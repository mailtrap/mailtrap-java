package io.mailtrap.client;

import io.mailtrap.client.api.*;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.BatchSendResponse;
import io.mailtrap.model.response.emails.SendResponse;
import io.mailtrap.util.SendingContextHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Client for interacting with Mailtrap APIs.
 * Emails might be sent using different dedicated APIs OR based on the configuration
 * provided by the {@link SendingContextHolder} and {@link MailtrapConfig} and {@link #send(MailtrapMail)}
 */
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapClient {

    /**
     * API for Mailtrap.io Sending functionality
     */
    @Getter
    private final MailtrapEmailSendingApi sendingApi;

    /**
     * API for Mailtrap.io Email Sandbox functionality
     */
    private final MailtrapEmailTestingApi sandboxApi;

    /**
     * API for Mailtrap.io Bulk Sending functionality
     */
    @Getter
    private final MailtrapBulkSendingApi bulkSendingApi;

    /**
     * API for Mailtrap.io General functionality
     */
    @Getter
    private final MailtrapGeneralApi generalApi;

    /**
     * API for Mailtrap.io Contacts functionality
     */
    @Getter
    private final MailtrapContactsApi contactsApi;

    /**
     * API for Mailtrap.io Email Templates management functionality. For the paginated
     * {@code /api/templates} endpoints, see {@link #templatesApi()}.
     */
    @Getter
    private final MailtrapEmailTemplatesApi emailTemplatesApi;

    /**
     * API for Mailtrap.io Templates management functionality (paginated {@code /api/templates} endpoints).
     * The endpoints are experimental: their request and response shapes may change before general availability.
     */
    @Getter
    private final MailtrapTemplatesApi templatesApi;

    /**
     * API for Mailtrap.io Organizations functionality
     */
    @Getter
    private final MailtrapOrganizationsApi organizationsApi;

    /**
     * API for Mailtrap.io Inbound Email functionality
     */
    @Getter
    private final MailtrapInboundApi inboundApi;

    /**
     * API for Mailtrap.io Email Campaigns functionality
     */
    @Getter
    private final MailtrapEmailCampaignsApi emailCampaignsApi;

    /**
     * API for Mailtrap.io Email Marketing functionality (aggregated campaign and automation stats)
     */
    @Getter
    private final MailtrapMarketingApi marketingApi;

    /**
     * Utility class which holds sending context (which API to use: Email Sending API, Bulk Sending API or
     * Email Sandbox API, sandbox ID for Email Sandbox API) to make it possible to perform send directly from MailtrapClient
     */
    private final SendingContextHolder sendingContextHolder;

    /**
     * @return API for Mailtrap.io Email Sandbox functionality
     */
    public MailtrapEmailTestingApi sandboxApi() {
        return sandboxApi;
    }

    /**
     * @deprecated Use {@link #sandboxApi()} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public MailtrapEmailTestingApi testingApi() {
        return sandboxApi();
    }

    /**
     * Sends an email based on the specified sending configuration.
     *
     * @param mailtrapMail the email to send
     * @return the response from the sending operation
     */
    public SendResponse send(final MailtrapMail mailtrapMail) {
        if (sendingContextHolder.isBulk()) {
            return bulkSendingApi.emails().send(mailtrapMail);
        } else if (sendingContextHolder.isSandbox()) {
            return sandboxApi.emails().send(mailtrapMail, sendingContextHolder.getSandboxId());
        } else {
            return sendingApi.emails().send(mailtrapMail);
        }
    }

    /**
     * Sends an email based on the specified sending configuration.
     *
     * @param mailtrapBatchMail emails to send
     * @return the response from the sending operation
     */
    public BatchSendResponse batchSend(final MailtrapBatchMail mailtrapBatchMail) {
        if (sendingContextHolder.isBulk()) {
            return bulkSendingApi.emails().batchSend(mailtrapBatchMail);
        } else if (sendingContextHolder.isSandbox()) {
            return sandboxApi.emails().batchSend(mailtrapBatchMail, sendingContextHolder.getSandboxId());
        } else {
            return sendingApi.emails().batchSend(mailtrapBatchMail);
        }
    }

    /**
     * Configures `send` method to use Bulk Sending API
     */
    public void switchToBulkSendingApi() {
        this.sendingContextHolder.setBulk(true);
        this.sendingContextHolder.setSandboxId(null);
        this.sendingContextHolder.setSandbox(false);
    }

    /**
     * Configures `send` method to use Email Sandbox API
     *
     * @param sandboxId the sandbox ID to send emails to
     */
    public void switchToEmailSandboxApi(final Long sandboxId) {
        this.sendingContextHolder.setSandboxId(sandboxId);
        this.sendingContextHolder.setSandbox(true);
        this.sendingContextHolder.setBulk(false);
    }

    /**
     * @deprecated Use {@link #switchToEmailSandboxApi(Long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public void switchToEmailTestingApi(final Long inboxId) {
        switchToEmailSandboxApi(inboxId);
    }

    /**
     * Configures `send` method to use Email Sending API.
     */
    public void switchToEmailSendingApi() {
        this.sendingContextHolder.setSandboxId(null);
        this.sendingContextHolder.setSandbox(false);
        this.sendingContextHolder.setBulk(false);
    }
}
