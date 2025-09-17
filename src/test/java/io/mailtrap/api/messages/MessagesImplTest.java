package io.mailtrap.api.messages;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.messages.ForwardMessageRequest;
import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;
import io.mailtrap.model.request.messages.UpdateMessageRequest;
import io.mailtrap.model.response.messages.*;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessagesImplTest extends BaseTest {

    private Messages api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId,
                        "GET", null, "api/messages/messageResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId,
                        "PATCH", "api/messages/updateMessageRequest.json", "api/messages/updatedMessageResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId,
                        "DELETE", null, "api/messages/messageResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages",
                        "GET", null, "api/messages/listMessagesResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages",
                        "GET", null, "api/messages/listMessagesWithSearchEmptyResponse.json", Map.of("search", "qqqqqqqq")),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages",
                        "GET", null, "api/messages/listMessagesWithSearchSingleEmailResponse.json",  Map.of("search", "mary")),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/forward",
                        "POST", "api/messages/forwardMessageRequest.json", "api/messages/forwardMessageResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/spam_report",
                        "GET", null, "api/messages/spamScoreResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/analyze",
                        "GET", null, "api/messages/htmlAnalysisResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/body.txt",
                        "GET", null, "api/messages/textMessageResponse.txt"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/body.raw",
                        "GET", null, "api/messages/rawMessageResponse.txt"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/body.htmlsource",
                        "GET", null, "api/messages/messageSourceResponse.txt"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/body.html",
                        "GET", null, "api/messages/htmlMessageResponse.txt"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/body.eml",
                        "GET", null, "api/messages/emlResponse.txt"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/mail_headers",
                        "GET", null, "api/messages/messageHeadersResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).testingApi().messages();
    }

    @Test
    void test_getMessage() {
        final MessageResponse message = api.getMessage(accountId, inboxId, messageId);

        assertNotNull(message);
        assertEquals(messageId, message.getId());
        assertFalse(message.isRead());
    }

    @Test
    void test_updateMessage() {
        final MessageResponse message = api.updateMessage(accountId, inboxId, messageId, new UpdateMessageRequest(new UpdateMessageRequest.MessageUpdateData("true")));

        assertNotNull(message);
        assertEquals(messageId, message.getId());
        assertTrue(message.isRead());
    }

    @Test
    void test_deleteMessage() {
        final MessageResponse message = api.deleteMessage(accountId, inboxId, messageId);

        assertNotNull(message);
        assertEquals(messageId, message.getId());
    }

    @Test
    void test_getMessages() {
        final List<MessageResponse> messages = api.getMessages(accountId, inboxId, ListMessagesQueryParams.empty());

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(messageId, messages.get(0).getId());
    }

    @Test
    void test_getMessagesWithSearchQueryParam_emptyResponse() {
        final ListMessagesQueryParams queryParams = new ListMessagesQueryParams();
        queryParams.setSearch("qqqqqqqq");

        final List<MessageResponse> messages = api.getMessages(accountId, inboxId, queryParams);

        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void test_getMessagesWithSearchQueryParam_notEmptyResponse() {
        final ListMessagesQueryParams queryParams = new ListMessagesQueryParams();
        queryParams.setSearch("mary");

        final List<MessageResponse> messages = api.getMessages(accountId, inboxId, queryParams);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(messageId, messages.get(0).getId());
    }

    @Test
    void test_forwardMessage() {
        final ForwardMessageResponse forwardMessageResponse = api.forwardMessage(accountId, inboxId, messageId, new ForwardMessageRequest("jack@mailtrap.io"));

        assertNotNull(forwardMessageResponse);
        assertEquals("Your email message has been successfully forwarded", forwardMessageResponse.getMessage());
    }

    @Test
    void test_getSpamScore() {
        final MessageSpamScoreResponse spamScore = api.getSpamScore(accountId, inboxId, messageId);

        assertNotNull(spamScore);
        assertEquals("Not spam", spamScore.getReport().getResponseMessage());
    }

    @Test
    void test_getMessageHtmlAnalysis() {
        final MessageHtmlAnalysisResponse htmlAnalysis = api.getMessageHtmlAnalysis(accountId, inboxId, messageId);

        assertNotNull(htmlAnalysis);
        assertEquals("success", htmlAnalysis.getReport().getStatus());
        assertEquals(2, htmlAnalysis.getReport().getErrors().size());
    }

    @Test
    void test_getTextMessage() {
        final String textMessage = api.getTextMessage(accountId, inboxId, messageId);

        assertNotNull(textMessage);
    }

    @Test
    void test_getRawMessage() {
        final String rawMessage = api.getRawMessage(accountId, inboxId, messageId);

        assertNotNull(rawMessage);
    }

    @Test
    void test_getMessageSource() {
        final String messageSource = api.getMessageSource(accountId, inboxId, messageId);

        assertNotNull(messageSource);
    }

    @Test
    void test_getHtmlMessage() {
        final String htmlMessage = api.getHtmlMessage(accountId, inboxId, messageId);

        assertNotNull(htmlMessage);
    }

    @Test
    void test_getMessageAsEml() {
        final String messageAsEml = api.getMessageAsEml(accountId, inboxId, messageId);

        assertNotNull(messageAsEml);
    }

    @Test
    void test_getMailHeaders() {
        final MessageHeadersResponse mailHeaders = api.getMailHeaders(accountId, inboxId, messageId);

        assertNotNull(mailHeaders);
        assertEquals(5, mailHeaders.getHeaders().size());
        assertNotNull(mailHeaders.getHeaders().get("cc"));
        assertEquals("john_doe@example.com", mailHeaders.getHeaders().get("cc"));
    }
}
