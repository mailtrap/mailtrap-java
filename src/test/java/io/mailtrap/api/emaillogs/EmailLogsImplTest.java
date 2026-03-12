package io.mailtrap.api.emaillogs;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emaillogs.EmailLogsListFilters;
import io.mailtrap.model.request.emaillogs.FilterExactString;
import io.mailtrap.model.request.emaillogs.FilterStatus;
import io.mailtrap.model.response.emaillogs.EmailLogEventType;
import io.mailtrap.model.response.emaillogs.EmailLogMessage;
import io.mailtrap.model.response.emaillogs.EmailLogMessageSummary;
import io.mailtrap.model.response.emaillogs.EmailLogsListResponse;
import io.mailtrap.model.response.emaillogs.MessageStatus;
import io.mailtrap.model.response.suppressions.SendingStream;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailLogsImplTest extends BaseTest {

    private static final String SENDING_MESSAGE_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";

    private EmailLogs api;

    @BeforeEach
    void init() {
        final String listUrl = Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_logs";
        final String listWithCursorUrl = listUrl + "?search_after=cursor-123";
        final String listWithStatusFilterUrl = listUrl
                + "?filters%5Bstatus%5D%5Boperator%5D=equal&filters%5Bstatus%5D%5Bvalue%5D=delivered";
        final String listWithMultipleValuesFilterUrl = listUrl
                + "?filters%5Bcategory%5D%5Boperator%5D=equal&filters%5Bcategory%5D%5Bvalue%5D%5B%5D=a"
                + "&filters%5Bcategory%5D%5Bvalue%5D%5B%5D=b";
        final String getMessageUrl = Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_logs/"
                + SENDING_MESSAGE_ID;

        final List<DataMock> mocks = List.of(
                DataMock.build(listUrl, "GET", null, "api/emaillogs/listEmailLogsResponse.json"),
                DataMock.build(listWithCursorUrl, "GET", null, "api/emaillogs/listEmailLogsResponse.json",
                        Collections.emptyMap()),
                DataMock.build(listWithStatusFilterUrl, "GET", null, "api/emaillogs/listEmailLogsResponse.json"),
                DataMock.build(listWithMultipleValuesFilterUrl, "GET", null, "api/emaillogs/listEmailLogsResponse.json"),
                DataMock.build(getMessageUrl, "GET", null, "api/emaillogs/getEmailLogMessageResponse.json"));

        final TestHttpClient httpClient = new TestHttpClient(mocks);
        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).sendingApi().emailLogs();
    }

    @Test
    void list_withNoParams_returnsPaginatedResponse() {
        final EmailLogsListResponse response = api.list(accountId, null, null);

        assertNotNull(response);
        assertNotNull(response.getMessages());
        assertEquals(1, response.getMessages().size());
        assertEquals(1, response.getTotalCount());
        assertNull(response.getNextPageCursor());

        final EmailLogMessageSummary msg = response.getMessages().get(0);
        assertEquals(SENDING_MESSAGE_ID, msg.getMessageId());
        assertEquals(MessageStatus.DELIVERED, msg.getStatus());
        assertEquals("Welcome to our service", msg.getSubject());
        assertEquals("sender@example.com", msg.getFrom());
        assertEquals("recipient@example.com", msg.getTo());
        assertEquals(SendingStream.TRANSACTIONAL, msg.getSendingStream());
        assertEquals(3938, msg.getSendingDomainId());
        assertEquals(2, msg.getOpensCount());
        assertEquals(1, msg.getClicksCount());
    }

    @Test
    void list_withSearchAfter_includesCursorInRequest() {
        final EmailLogsListResponse response = api.list(accountId, "cursor-123", null);

        assertNotNull(response);
        assertEquals(1, response.getMessages().size());
    }

    @Test
    void list_withFilters_buildsQueryWithOperatorAndValue() {
        final var filters = EmailLogsListFilters.builder()
                .status(new FilterStatus(FilterStatus.Operator.equal, MessageStatus.DELIVERED))
                .build();
        final EmailLogsListResponse response = api.list(accountId, null, filters);

        assertNotNull(response);
        assertEquals(1, response.getMessages().size());
    }

    @Test
    void list_withMultipleValuesFilter_usesBracketNotationInQuery() {
        final var filters = EmailLogsListFilters.builder()
                .category(new FilterExactString(FilterExactString.Operator.equal, List.of("a", "b")))
                .build();
        final EmailLogsListResponse response = api.list(accountId, null, filters);

        assertNotNull(response);
        assertEquals(1, response.getMessages().size());
    }

    @Test
    void get_byMessageId_returnsEmailLogMessage() {
        final EmailLogMessage message = api.get(accountId, SENDING_MESSAGE_ID);

        assertNotNull(message);
        assertEquals(SENDING_MESSAGE_ID, message.getMessageId());
        assertEquals(MessageStatus.DELIVERED, message.getStatus());
        assertEquals("Welcome to our service", message.getSubject());
        assertNotNull(message.getRawMessageUrl());
        assertNotNull(message.getEvents());
        assertEquals(1, message.getEvents().size());
        assertEquals(EmailLogEventType.CLICK, message.getEvents().get(0).getEventType());
    }

    @Test
    void get_withNullMessageId_throws() {
        assertThrows(IllegalArgumentException.class, () -> api.get(accountId, null));
    }

    @Test
    void get_withBlankMessageId_throws() {
        assertThrows(IllegalArgumentException.class, () -> api.get(accountId, "   "));
    }
}
