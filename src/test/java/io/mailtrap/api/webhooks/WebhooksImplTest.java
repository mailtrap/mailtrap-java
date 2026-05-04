package io.mailtrap.api.webhooks;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.SendingStream;
import io.mailtrap.model.WebhookEventType;
import io.mailtrap.model.WebhookPayloadFormat;
import io.mailtrap.model.WebhookType;
import io.mailtrap.model.request.webhooks.CreateWebhookRequest;
import io.mailtrap.model.request.webhooks.UpdateWebhookRequest;
import io.mailtrap.model.request.webhooks.WebhookInput;
import io.mailtrap.model.response.webhooks.CreateWebhookResponse;
import io.mailtrap.model.response.webhooks.ListWebhooksResponse;
import io.mailtrap.model.response.webhooks.WebhookResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebhooksImplTest extends BaseTest {

    private final long webhookId = 1L;

    private Webhooks api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/webhooks",
                        "POST", "api/webhooks/createWebhookRequest.json", "api/webhooks/createWebhookResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/webhooks",
                        "GET", null, "api/webhooks/listWebhooksResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/webhooks/" + webhookId,
                        "GET", null, "api/webhooks/getWebhookResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/webhooks/" + webhookId,
                        "PATCH", "api/webhooks/updateWebhookRequest.json", "api/webhooks/updateWebhookResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/webhooks/" + webhookId,
                        "DELETE", null, "api/webhooks/deleteWebhookResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().webhooks();
    }

    @Test
    void test_createWebhook() {
        final CreateWebhookRequest request = new CreateWebhookRequest(
                WebhookInput.builder()
                        .url("https://example.com/mailtrap/webhooks")
                        .webhookType(WebhookType.EMAIL_SENDING)
                        .payloadFormat(WebhookPayloadFormat.JSON)
                        .sendingStream(SendingStream.TRANSACTIONAL)
                        .eventTypes(List.of(WebhookEventType.DELIVERY, WebhookEventType.BOUNCE))
                        .domainId(435L)
                        .build()
        );

        final CreateWebhookResponse response = api.createWebhook(accountId, request);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(webhookId, response.getData().getId());
        assertEquals("a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6", response.getData().getSigningSecret());
        assertEquals(WebhookType.EMAIL_SENDING, response.getData().getWebhookType());
        assertEquals(SendingStream.TRANSACTIONAL, response.getData().getSendingStream());
        assertTrue(response.getData().isActive());
    }

    @Test
    void test_getAllWebhooks() {
        final ListWebhooksResponse response = api.getAllWebhooks(accountId);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(WebhookType.EMAIL_SENDING, response.getData().get(0).getWebhookType());
        assertEquals(WebhookType.AUDIT_LOG, response.getData().get(1).getWebhookType());
    }

    @Test
    void test_getWebhook() {
        final WebhookResponse response = api.getWebhook(accountId, webhookId);

        assertNotNull(response);
        assertEquals(webhookId, response.getData().getId());
        assertEquals(435L, response.getData().getDomainId());
        assertEquals(List.of(WebhookEventType.DELIVERY, WebhookEventType.BOUNCE), response.getData().getEventTypes());
    }

    @Test
    void test_updateWebhook() {
        final UpdateWebhookRequest request = new UpdateWebhookRequest(
                WebhookInput.builder().active(false).build()
        );

        final WebhookResponse response = api.updateWebhook(accountId, webhookId, request);

        assertNotNull(response);
        assertEquals(webhookId, response.getData().getId());
        assertFalse(response.getData().isActive());
    }

    @Test
    void test_deleteWebhook() {
        final WebhookResponse response = api.deleteWebhook(accountId, webhookId);

        assertNotNull(response);
        assertEquals(webhookId, response.getData().getId());
        assertEquals(WebhookType.AUDIT_LOG, response.getData().getWebhookType());
    }
}
