package io.mailtrap.api.inbound;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.emaillogs.MessageStatus;
import io.mailtrap.model.response.inbound.InboundForwardOutcome;
import io.mailtrap.model.response.inbound.InboundForwardOutcomeStatus;
import io.mailtrap.model.response.inbound.InboundMessageDirection;
import io.mailtrap.model.response.inbound.InboundThread;
import io.mailtrap.model.response.inbound.InboundThreadMessage;
import io.mailtrap.model.response.inbound.InboundThreadMessageDelivery;
import io.mailtrap.model.response.inbound.InboundThreadsListResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class InboundThreadsImplTest extends BaseTest {

    private static final long INBOX_ID = 201L;
    private static final String THREAD_ID = "thr_1";

    private InboundThreads api;

    @BeforeEach
    void init() {
        final String threadsUrl = Constants.GENERAL_HOST + "/api/inbound/inboxes/" + INBOX_ID + "/threads";
        final String threadUrl = threadsUrl + "/" + THREAD_ID;

        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(threadsUrl, "GET", null, "api/inbound/listInboundThreadsResponse.json"),
                DataMock.build(threadsUrl, "GET", null, "api/inbound/listInboundThreadsResponse.json",
                        Map.of("last_id", "thr_2")),
                DataMock.build(threadsUrl, "GET", null, "api/inbound/listInboundThreadsResponse.json",
                        Map.of("search", "acme")),
                DataMock.build(threadsUrl, "GET", null, "api/inbound/listInboundThreadsResponse.json",
                        Map.of("last_id", "thr_2", "search", "acme")),
                DataMock.build(threadUrl, "GET", null, "api/inbound/getInboundThreadResponse.json"),
                DataMock.build(threadUrl, "DELETE", null, null)
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).inboundApi().threads();
    }

    @Test
    void list_withoutCursor_returnsPage() {
        final InboundThreadsListResponse response = api.list(INBOX_ID, null);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(2, response.getTotalCount());
        assertEquals("thr_2", response.getLastId());
        assertEquals("Support request", response.getData().get(0).getSubject());
        assertEquals(3, response.getData().get(0).getMessageCount());
    }

    @Test
    void list_withCursor_returnsPage() {
        final InboundThreadsListResponse response = api.list(INBOX_ID, "thr_2");

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void list_withSearch_returnsPage() {
        final InboundThreadsListResponse response = api.list(INBOX_ID, null, "acme");

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void list_withSearchAndCursor_returnsPage() {
        final InboundThreadsListResponse response = api.list(INBOX_ID, "thr_2", "acme");

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void list_withNullSearch_omitsSearchParam() {
        final InboundThreadsListResponse response = api.list(INBOX_ID, "thr_2", null);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void get_returnsThreadWithMessages() {
        final InboundThread thread = api.get(INBOX_ID, THREAD_ID);

        assertNotNull(thread);
        assertEquals("thr_1", thread.getId());
        assertEquals("Support request", thread.getSubject());
        assertNotNull(thread.getMessages());
        assertEquals(2, thread.getMessages().size());
        assertEquals(InboundMessageDirection.INBOUND, thread.getMessages().get(0).getDirection());
        assertEquals(InboundMessageDirection.OUTBOUND, thread.getMessages().get(1).getDirection());
    }

    @Test
    void get_deserializesForwardsOnInboundMessages() {
        final InboundThreadMessage inbound = api.get(INBOX_ID, THREAD_ID).getMessages().get(0);

        assertNull(inbound.getDelivery());
        assertEquals(2, inbound.getForwards().size());

        final InboundForwardOutcome forwarded = inbound.getForwards().get(0);
        assertEquals(7L, forwarded.getRuleId());
        assertEquals("Copy to support team", forwarded.getRuleName());
        assertEquals("team@example.com", forwarded.getDestination());
        assertEquals(InboundForwardOutcomeStatus.FORWARDED, forwarded.getStatus());
        assertNull(forwarded.getReason());
        assertEquals("f47ac10b-58cc-4372-a567-0e02b2c3d479", forwarded.getMessageId());

        final InboundForwardOutcome rejected = inbound.getForwards().get(1);
        assertNull(rejected.getRuleName());
        assertEquals(InboundForwardOutcomeStatus.REJECTED, rejected.getStatus());
        assertEquals("loop_prevention", rejected.getReason());
        assertNull(rejected.getMessageId());
    }

    @Test
    void get_deserializesDeliveryOnOutboundMessages() {
        final InboundThreadMessage outbound = api.get(INBOX_ID, THREAD_ID).getMessages().get(1);

        assertNull(outbound.getForwards());
        final InboundThreadMessageDelivery delivery = outbound.getDelivery();
        assertNotNull(delivery);
        assertEquals("customer@example.com", delivery.getTo());
        assertEquals(MessageStatus.DELIVERED, delivery.getStatus());
        assertEquals(OffsetDateTime.parse("2026-07-30T13:00:05Z"), delivery.getDeliveredAt());
        assertNull(delivery.getBouncedAt());
    }

    @Test
    void delete_doesNotThrow() {
        assertDoesNotThrow(() -> api.delete(INBOX_ID, THREAD_ID));
    }
}
