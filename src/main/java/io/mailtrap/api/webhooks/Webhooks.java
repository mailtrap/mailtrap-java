package io.mailtrap.api.webhooks;

import io.mailtrap.model.request.webhooks.CreateWebhookRequest;
import io.mailtrap.model.request.webhooks.UpdateWebhookRequest;
import io.mailtrap.model.response.webhooks.CreateWebhookResponse;
import io.mailtrap.model.response.webhooks.ListWebhooksResponse;
import io.mailtrap.model.response.webhooks.WebhookResponse;

public interface Webhooks {

    /**
     * Create a new webhook for the account. The response includes the
     * {@code signing_secret} used to verify webhook signatures (only returned on creation).
     *
     * @param accountId unique account ID
     * @param request   webhook configuration
     * @return created webhook including the signing secret
     */
    CreateWebhookResponse createWebhook(long accountId, CreateWebhookRequest request);

    /**
     * List all webhooks for the account.
     *
     * @param accountId unique account ID
     * @return webhooks
     */
    ListWebhooksResponse getAllWebhooks(long accountId);

    /**
     * Get a single webhook by ID.
     *
     * @param accountId unique account ID
     * @param webhookId webhook ID
     * @return webhook details
     */
    WebhookResponse getWebhook(long accountId, long webhookId);

    /**
     * Update an existing webhook.
     *
     * @param accountId unique account ID
     * @param webhookId webhook ID
     * @param request   fields to update
     * @return updated webhook
     */
    WebhookResponse updateWebhook(long accountId, long webhookId, UpdateWebhookRequest request);

    /**
     * Permanently delete a webhook.
     *
     * @param accountId unique account ID
     * @param webhookId webhook ID
     * @return the deleted webhook
     */
    WebhookResponse deleteWebhook(long accountId, long webhookId);

}
