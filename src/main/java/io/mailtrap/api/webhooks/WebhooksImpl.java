package io.mailtrap.api.webhooks;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.webhooks.CreateWebhookRequest;
import io.mailtrap.model.request.webhooks.UpdateWebhookRequest;
import io.mailtrap.model.response.webhooks.CreateWebhookResponse;
import io.mailtrap.model.response.webhooks.ListWebhooksResponse;
import io.mailtrap.model.response.webhooks.WebhookResponse;

public class WebhooksImpl extends ApiResource implements Webhooks {

    public WebhooksImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public CreateWebhookResponse createWebhook(final long accountId, final CreateWebhookRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/webhooks", accountId),
            request,
            new RequestData(),
            CreateWebhookResponse.class
        );
    }

    @Override
    public ListWebhooksResponse getAllWebhooks(final long accountId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/webhooks", accountId),
            new RequestData(),
            ListWebhooksResponse.class
        );
    }

    @Override
    public WebhookResponse getWebhook(final long accountId, final long webhookId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/webhooks/%d", accountId, webhookId),
            new RequestData(),
            WebhookResponse.class
        );
    }

    @Override
    public WebhookResponse updateWebhook(final long accountId, final long webhookId, final UpdateWebhookRequest request) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/webhooks/%d", accountId, webhookId),
            request,
            new RequestData(),
            WebhookResponse.class
        );
    }

    @Override
    public WebhookResponse deleteWebhook(final long accountId, final long webhookId) {
        return httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/webhooks/%d", accountId, webhookId),
            new RequestData(),
            WebhookResponse.class
        );
    }
}
