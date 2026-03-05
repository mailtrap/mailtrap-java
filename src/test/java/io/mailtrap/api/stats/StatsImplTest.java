package io.mailtrap.api.stats;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.stats.SendingStatGroupResponse;
import io.mailtrap.model.response.stats.SendingStatsResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatsImplTest extends BaseTest {

    private Stats api;

    private final String startDate = "2026-01-01";
    private final String endDate = "2026-01-31";
    private final String baseUrl = Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/stats";

    @BeforeEach
    public void init() {
        final Map<String, Object> defaultQueryParams = Map.of(
            "start_date", startDate,
            "end_date", endDate
        );

        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(baseUrl, "GET", null, "api/stats/getStats.json", defaultQueryParams),
            DataMock.build(baseUrl + "/domains", "GET", null, "api/stats/byDomains.json", defaultQueryParams),
            DataMock.build(baseUrl + "/categories", "GET", null, "api/stats/byCategories.json", defaultQueryParams),
            DataMock.build(baseUrl + "/email_service_providers", "GET", null, "api/stats/byEmailServiceProviders.json", defaultQueryParams),
            DataMock.build(baseUrl + "/date", "GET", null, "api/stats/byDate.json", defaultQueryParams)
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().stats();
    }

    @Test
    void test_getStats() {
        final StatsFilter filter = StatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();

        final SendingStatsResponse response = api.getStats(accountId, filter);

        assertNotNull(response);
        assertEquals(150, response.getDeliveryCount());
        assertEquals(0.95, response.getDeliveryRate());
        assertEquals(8, response.getBounceCount());
        assertEquals(0.05, response.getBounceRate());
        assertEquals(120, response.getOpenCount());
        assertEquals(0.8, response.getOpenRate());
        assertEquals(60, response.getClickCount());
        assertEquals(0.5, response.getClickRate());
        assertEquals(2, response.getSpamCount());
        assertEquals(0.013, response.getSpamRate());
    }

    @Test
    void test_byDomains() {
        final StatsFilter filter = StatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();

        final List<SendingStatGroupResponse> response = api.byDomains(accountId, filter);

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("sending_domain_id", response.get(0).getName());
        assertEquals(1, response.get(0).getValue());
        assertEquals(100, response.get(0).getStats().getDeliveryCount());
        assertEquals(0.96, response.get(0).getStats().getDeliveryRate());

        assertEquals("sending_domain_id", response.get(1).getName());
        assertEquals(2, response.get(1).getValue());
        assertEquals(50, response.get(1).getStats().getDeliveryCount());
    }

    @Test
    void test_byCategories() {
        final StatsFilter filter = StatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();

        final List<SendingStatGroupResponse> response = api.byCategories(accountId, filter);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("category", response.get(0).getName());
        assertEquals("Transactional", response.get(0).getValue());
        assertEquals(100, response.get(0).getStats().getDeliveryCount());
        assertEquals(0.97, response.get(0).getStats().getDeliveryRate());
    }

    @Test
    void test_byEmailServiceProviders() {
        final StatsFilter filter = StatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();

        final List<SendingStatGroupResponse> response = api.byEmailServiceProviders(accountId, filter);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("email_service_provider", response.get(0).getName());
        assertEquals("Gmail", response.get(0).getValue());
        assertEquals(80, response.get(0).getStats().getDeliveryCount());
        assertEquals(0.97, response.get(0).getStats().getDeliveryRate());
    }

    @Test
    void test_byDate() {
        final StatsFilter filter = StatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();

        final List<SendingStatGroupResponse> response = api.byDate(accountId, filter);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("date", response.get(0).getName());
        assertEquals("2026-01-01", response.get(0).getValue());
        assertEquals(5, response.get(0).getStats().getDeliveryCount());
        assertEquals(1.0, response.get(0).getStats().getDeliveryRate());
    }
}
