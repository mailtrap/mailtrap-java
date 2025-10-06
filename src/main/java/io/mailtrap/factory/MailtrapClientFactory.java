package io.mailtrap.factory;

import io.mailtrap.MailtrapValidator;
import io.mailtrap.api.accountaccesses.AccountAccessesImpl;
import io.mailtrap.api.accounts.AccountsImpl;
import io.mailtrap.api.attachments.AttachmentsImpl;
import io.mailtrap.api.billing.BillingImpl;
import io.mailtrap.api.bulkemails.BulkEmailsImpl;
import io.mailtrap.api.contactexports.ContactExportsImpl;
import io.mailtrap.api.contactfields.ContactFieldsImpl;
import io.mailtrap.api.contactimports.ContactImportsImpl;
import io.mailtrap.api.contactlists.ContactListsImpl;
import io.mailtrap.api.contacts.ContactsImpl;
import io.mailtrap.api.emailtemplates.EmailTemplatesImpl;
import io.mailtrap.api.inboxes.InboxesImpl;
import io.mailtrap.api.messages.MessagesImpl;
import io.mailtrap.api.permissions.PermissionsImpl;
import io.mailtrap.api.projects.ProjectsImpl;
import io.mailtrap.api.sendingdomains.SendingDomainsImpl;
import io.mailtrap.api.sendingemails.SendingEmailsImpl;
import io.mailtrap.api.suppressions.SuppressionsImpl;
import io.mailtrap.api.testingemails.TestingEmailsImpl;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.client.api.*;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.util.SendingContextHolder;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Factory class for creating instances of {@link MailtrapClient}.
 */
public final class MailtrapClientFactory {

    /**
     * Creates a new instance of {@link MailtrapValidator} using the default validator factory.
     * Intentionally not wrapped into try-with-resources to not close, as per Jakarta doc, after
     * the {@code ValidatorFactory} instance is closed, calling the following methods is not allowed:
     * <ul>
     *     <li>methods of this {@code ValidatorFactory} instance</li>
     *     <li>methods of {@link Validator} instances created by this
     *     {@code ValidatorFactory}</li>
     * </ul>
     */
    private static final jakarta.validation.ValidatorFactory VALIDATOR_FACTORY =
        Validation.buildDefaultValidatorFactory();
    
    private static final MailtrapValidator VALIDATOR =
        new MailtrapValidator(VALIDATOR_FACTORY.getValidator());

    private MailtrapClientFactory() {
    }

    /**
     * Creates a new {@link MailtrapClient} instance.
     *
     * @param config The configuration for creating the Mailtrap client.
     * @return A new Mailtrap client instance.
     */
    public static MailtrapClient createMailtrapClient(final MailtrapConfig config) {
        final var sendingApi = createSendingApi(config);
        final var testingApi = createTestingApi(config);
        final var bulkSendingApi = createBulkSendingApi(config);
        final var generalApi = createGeneralApi(config);
        final var contactsApi = createContactsApi(config);
        final var emailTemplatesApi = createEmailTemplatesApi(config);

        final var sendingContextHolder = configureSendingContext(config);

        return new MailtrapClient(sendingApi, testingApi, bulkSendingApi, generalApi, contactsApi, emailTemplatesApi, sendingContextHolder);
    }

    private static MailtrapContactsApi createContactsApi(final MailtrapConfig config) {
        final var contactLists = new ContactListsImpl(config);
        final var contacts = new ContactsImpl(config);
        final var contactImports = new ContactImportsImpl(config, VALIDATOR);
        final var contactFields = new ContactFieldsImpl(config, VALIDATOR);
        final var contactExports = new ContactExportsImpl(config);

        return new MailtrapContactsApi(contactLists, contacts, contactImports, contactFields, contactExports);
    }

    private static MailtrapGeneralApi createGeneralApi(final MailtrapConfig config) {
        final var accountAccess = new AccountAccessesImpl(config);
        final var accounts = new AccountsImpl(config);
        final var billing = new BillingImpl(config);
        final var permissions = new PermissionsImpl(config);

        return new MailtrapGeneralApi(accountAccess, accounts, billing, permissions);
    }

    private static MailtrapEmailSendingApi createSendingApi(final MailtrapConfig config) {
        final var emails = new SendingEmailsImpl(config, VALIDATOR);
        final var domains = new SendingDomainsImpl(config);
        final var suppressions = new SuppressionsImpl(config);

        return new MailtrapEmailSendingApi(emails, domains, suppressions);
    }

    private static MailtrapEmailTestingApi createTestingApi(final MailtrapConfig config) {
        final var emails = new TestingEmailsImpl(config, VALIDATOR);
        final var attachments = new AttachmentsImpl(config);
        final var inboxes = new InboxesImpl(config, VALIDATOR);
        final var projects = new ProjectsImpl(config, VALIDATOR);
        final var messages = new MessagesImpl(config);

        return new MailtrapEmailTestingApi(emails, attachments, inboxes, projects, messages);
    }

    private static MailtrapBulkSendingApi createBulkSendingApi(final MailtrapConfig config) {
        final var emails = new BulkEmailsImpl(config, VALIDATOR);

        return new MailtrapBulkSendingApi(emails);
    }

    private static MailtrapEmailTemplatesApi createEmailTemplatesApi(final MailtrapConfig config) {
        final var emailTemplates = new EmailTemplatesImpl(config, VALIDATOR);

        return new MailtrapEmailTemplatesApi(emailTemplates);
    }

    private static SendingContextHolder configureSendingContext(final MailtrapConfig config) {

        return SendingContextHolder.builder()
            .sandbox(config.isSandbox())
            .inboxId(config.getInboxId())
            .bulk(config.isBulk())
            .build();
    }
}
