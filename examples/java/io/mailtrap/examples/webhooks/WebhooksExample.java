package io.mailtrap.examples.webhooks;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.SendingStream;
import io.mailtrap.model.WebhookEventType;
import io.mailtrap.model.WebhookPayloadFormat;
import io.mailtrap.model.WebhookType;
import io.mailtrap.model.request.webhooks.CreateWebhookRequest;
import io.mailtrap.model.request.webhooks.UpdateWebhookRequest;
import io.mailtrap.model.request.webhooks.WebhookInput;

import java.util.List;

public class WebhooksExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final String WEBHOOK_URL = "https://example.com/webhooks/mailtrap";
    private static final String UPDATED_WEBHOOK_URL = "https://example.com/webhooks/mailtrap/v2";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var createRequest = new CreateWebhookRequest(
            WebhookInput.builder()
                .url(WEBHOOK_URL)
                .webhookType(WebhookType.EMAIL_SENDING)
                .active(true)
                .payloadFormat(WebhookPayloadFormat.JSON)
                .sendingStream(SendingStream.TRANSACTIONAL)
                .eventTypes(List.of(
                    WebhookEventType.DELIVERY,
                    WebhookEventType.BOUNCE,
                    WebhookEventType.OPEN,
                    WebhookEventType.CLICK,
                    WebhookEventType.UNSUBSCRIBE))
                .build());

        // `signing_secret` is only returned on creation — store it securely.
        final var createResponse = client.generalApi().webhooks()
            .createWebhook(ACCOUNT_ID, createRequest);
        System.out.println(createResponse);

        final var webhookId = createResponse.getData().getId();

        final var allWebhooks = client.generalApi().webhooks().getAllWebhooks(ACCOUNT_ID);
        System.out.println(allWebhooks);

        final var webhook = client.generalApi().webhooks().getWebhook(ACCOUNT_ID, webhookId);
        System.out.println(webhook);

        final var updateRequest = new UpdateWebhookRequest(
            WebhookInput.builder()
                .url(UPDATED_WEBHOOK_URL)
                .active(false)
                .eventTypes(List.of(WebhookEventType.DELIVERY, WebhookEventType.BOUNCE))
                .build());

        final var updateResponse = client.generalApi().webhooks()
            .updateWebhook(ACCOUNT_ID, webhookId, updateRequest);
        System.out.println(updateResponse);

        final var deleteResponse = client.generalApi().webhooks().deleteWebhook(ACCOUNT_ID, webhookId);
        System.out.println(deleteResponse);
    }
}
