package io.mailtrap.api.inbound;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.inbound.InboundForwardRequest;
import io.mailtrap.model.request.inbound.InboundReplyRequest;
import io.mailtrap.model.response.inbound.InboundForwardOutcome;
import io.mailtrap.model.response.inbound.InboundForwardOutcomeStatus;
import io.mailtrap.model.response.inbound.InboundMessage;
import io.mailtrap.model.response.inbound.InboundMessagesListResponse;
import io.mailtrap.model.response.inbound.InboundSendResult;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InboundMessagesImplTest extends BaseTest {

    private static final long INBOX_ID = 201L;
    private static final String MESSAGE_ID = "msg_1";

    private InboundMessages api;

    @BeforeEach
    void init() {
        final String messagesUrl = Constants.GENERAL_HOST + "/api/inbound/inboxes/" + INBOX_ID + "/messages";
        final String messageUrl = messagesUrl + "/" + MESSAGE_ID;

        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(messagesUrl, "GET", null, "api/inbound/listInboundMessagesResponse.json"),
                DataMock.build(messagesUrl, "GET", null, "api/inbound/listInboundMessagesResponse.json",
                        Map.of("last_id", "msg_2")),
                DataMock.build(messageUrl, "GET", null, "api/inbound/getInboundMessageResponse.json"),
                DataMock.build(messageUrl, "DELETE", null, null),
                DataMock.build(messageUrl + "/reply", "POST", "api/inbound/replyInboundMessageRequest.json",
                        "api/inbound/sendInboundMessageResponse.json"),
                DataMock.build(messageUrl + "/reply_all", "POST", "api/inbound/replyAllInboundMessageRequest.json",
                        "api/inbound/sendInboundMessageResponse.json"),
                DataMock.build(messageUrl + "/forward", "POST", "api/inbound/forwardInboundMessageRequest.json",
                        "api/inbound/sendInboundMessageResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).inboundApi().messages();
    }

    @Test
    void list_withoutCursor_returnsPage() {
        final InboundMessagesListResponse response = api.list(INBOX_ID, null);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(2, response.getTotalCount());
        assertEquals("msg_2", response.getLastId());
        assertEquals("customer@example.com", response.getData().get(0).getFrom());
        assertEquals("thr_1", response.getData().get(0).getThreadId());

        final List<InboundForwardOutcome> forwards = response.getData().get(0).getForwards();
        assertEquals(1, forwards.size());
        assertEquals(7L, forwards.get(0).getRuleId());
        assertEquals("Copy to support team", forwards.get(0).getRuleName());
        assertEquals("team@example.com", forwards.get(0).getDestination());
        assertEquals(InboundForwardOutcomeStatus.FORWARDED, forwards.get(0).getStatus());
        assertNull(forwards.get(0).getReason());
        assertEquals("f47ac10b-58cc-4372-a567-0e02b2c3d479", forwards.get(0).getMessageId());
        assertTrue(response.getData().get(1).getForwards().isEmpty());
    }

    @Test
    void list_withCursor_returnsPage() {
        final InboundMessagesListResponse response = api.list(INBOX_ID, "msg_2");

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void get_returnsMessageWithBodyAndAttachments() {
        final InboundMessage message = api.get(INBOX_ID, MESSAGE_ID);

        assertNotNull(message);
        assertEquals("msg_1", message.getId());
        assertEquals(201L, message.getInboxId());
        assertEquals("Support request", message.getSubject());
        assertEquals("<p>Hello, I need help.</p>", message.getHtmlBody());
        assertNotNull(message.getReceivedAt());
        assertNotNull(message.getAttachments());
        assertEquals(1, message.getAttachments().size());
        assertEquals("invoice.pdf", message.getAttachments().get(0).getFilename());
        assertEquals("https://example.com/download/att_1", message.getAttachments().get(0).getDownloadUrl());
        assertEquals(1, message.getForwards().size());
        assertEquals(InboundForwardOutcomeStatus.REJECTED, message.getForwards().get(0).getStatus());
        assertEquals("fetch_failed", message.getForwards().get(0).getReason());
        assertNull(message.getForwards().get(0).getMessageId());
    }

    @Test
    void delete_doesNotThrow() {
        assertDoesNotThrow(() -> api.delete(INBOX_ID, MESSAGE_ID));
    }

    @Test
    void reply_returnsSendResult() {
        final InboundReplyRequest request = InboundReplyRequest.builder()
                .to(List.of(new Address("customer@example.com")))
                .text("Thanks for reaching out, we are on it!")
                .build();

        final InboundSendResult result = api.reply(INBOX_ID, MESSAGE_ID, request);

        assertNotNull(result);
        assertEquals(List.of("0000000000000001"), result.getMessageIds());
    }

    @Test
    void replyAll_returnsSendResult() {
        final InboundReplyRequest request = InboundReplyRequest.builder()
                .to(List.of(new Address("customer@example.com")))
                .cc(List.of(new Address("cc@example.com")))
                .text("Thanks everyone!")
                .build();

        final InboundSendResult result = api.replyAll(INBOX_ID, MESSAGE_ID, request);

        assertNotNull(result);
        assertEquals(List.of("0000000000000001"), result.getMessageIds());
    }

    @Test
    void forward_returnsSendResult() {
        final InboundForwardRequest request = InboundForwardRequest.builder()
                .to(List.of(new Address("teammate@example.com")))
                .text("FYI, please take a look.")
                .build();

        final InboundSendResult result = api.forward(INBOX_ID, MESSAGE_ID, request);

        assertNotNull(result);
        assertEquals(List.of("0000000000000001"), result.getMessageIds());
    }

    @Test
    void forward_withoutRecipients_throws() {
        final InboundForwardRequest request = InboundForwardRequest.builder()
                .text("FYI")
                .build();

        assertThrows(InvalidRequestBodyException.class, () -> api.forward(INBOX_ID, MESSAGE_ID, request));
    }
}
