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
     * API for Mailtrap.io Testing functionality
     */
    @Getter
    private final MailtrapEmailTestingApi testingApi;

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
     * API for Mailtrap.io Email Templates management functionality
     */
    @Getter
    private final MailtrapEmailTemplatesApi emailTemplatesApi;

    /**
     * Utility class which holds sending context (which API to use: Email Sending API, Bulk Sending API or
     * Email Testing API, inbox id for Email Testing API) to make it possible to perform send directly from MailtrapClient
     */
    private final SendingContextHolder sendingContextHolder;

    /**
     * Sends an email based on the specified sending configuration.
     *
     * @param mailtrapMail the email to send
     * @return the response from the sending operation
     */
    public SendResponse send(MailtrapMail mailtrapMail) {
        if (sendingContextHolder.isBulk()) {
            return bulkSendingApi.emails().send(mailtrapMail);
        } else if (sendingContextHolder.isSandbox()) {
            return testingApi.emails().send(mailtrapMail, sendingContextHolder.getInboxId());
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
    public BatchSendResponse batchSend(MailtrapBatchMail mailtrapBatchMail) {
        if (sendingContextHolder.isBulk()) {
            return bulkSendingApi.emails().batchSend(mailtrapBatchMail);
        } else if (sendingContextHolder.isSandbox()) {
            return testingApi.emails().batchSend(mailtrapBatchMail, sendingContextHolder.getInboxId());
        } else {
            return sendingApi.emails().batchSend(mailtrapBatchMail);
        }
    }

    /**
     * Configures `send` method to use Bulk Sending API
     */
    public void switchToBulkSendingApi() {
        this.sendingContextHolder.setBulk(true);
        this.sendingContextHolder.setInboxId(null);
        this.sendingContextHolder.setSandbox(false);
    }

    /**
     * Configures `send` method to use Email Testing API
     *
     * @param inboxId the inbox ID to use for testing
     */
    public void switchToEmailTestingApi(Long inboxId) {
        this.sendingContextHolder.setInboxId(inboxId);
        this.sendingContextHolder.setSandbox(true);
        this.sendingContextHolder.setBulk(false);
    }

    /**
     * Configures `send` method to use Email Sending API.
     */
    public void switchToEmailSendingApi() {
        this.sendingContextHolder.setInboxId(null);
        this.sendingContextHolder.setSandbox(false);
        this.sendingContextHolder.setBulk(false);
    }
}
