package io.mailtrap.api.marketingstats;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.MarketingStatsSource;
import io.mailtrap.model.response.marketingstats.MarketingStatsData;
import io.mailtrap.model.response.marketingstats.MarketingStatsResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketingStatsImplTest extends BaseTest {

    private final String startDate = "2026-05-01";
    private final String endDate = "2026-05-31";
    private final String url = Constants.GENERAL_HOST + "/api/marketing/stats";

    private TestHttpClient httpClient;
    private MarketingStats api;

    @BeforeEach
    public void init() {
        httpClient = new TestHttpClient(List.of(
            DataMock.build(url, "GET", null, "api/marketingstats/getMarketingStatsResponse.json",
                Map.of("start_date", startDate, "end_date", endDate)),

            DataMock.build(url, "GET", null, "api/marketingstats/getMarketingStatsFilteredResponse.json",
                Map.of(
                    "start_date", startDate,
                    "end_date", endDate,
                    "sending_domain_ids[]", List.of(sendingDomainId),
                    "sources[]", List.of(MarketingStatsSource.CAMPAIGNS),
                    "email_service_providers[]", List.of("Google", "Yahoo")
                ))
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).marketingApi().stats();
    }

    @Test
    void test_getMarketingStats() {
        final MarketingStatsResponse response = api.getMarketingStats(MarketingStatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build());

        assertNotNull(response);
        final MarketingStatsData data = response.getData();
        assertEquals(1200, data.getDeliveryCount());
        assertEquals(0.96, data.getDeliveryRate());
        assertEquals(50, data.getBounceCount());
        assertEquals(0.04, data.getBounceRate());
        assertEquals(600, data.getOpenCount());
        assertEquals(0.5, data.getOpenRate());
        assertEquals(240, data.getClickCount());
        assertEquals(0.2, data.getClickRate());
        assertEquals(3, data.getSpamCount());
        assertEquals(0.0025, data.getSpamRate());
        assertEquals(12, data.getUnsubscriptionCount());
        assertEquals(0.01, data.getUnsubscriptionRate());
        assertEquals(1250, data.getSentCount());
    }

    @Test
    void test_getMarketingStats_withFilters() {
        final MarketingStatsResponse response = api.getMarketingStats(MarketingStatsFilter.builder()
            .startDate(startDate)
            .endDate(endDate)
            .sendingDomainIds(List.of(sendingDomainId))
            .sources(List.of(MarketingStatsSource.CAMPAIGNS))
            .emailServiceProviders(List.of("Google", "Yahoo"))
            .build());

        assertNotNull(response);
        assertEquals(800, response.getData().getDeliveryCount());
        assertEquals(820, response.getData().getSentCount());
        assertEquals(0.525, response.getData().getOpenRate());
    }

    // TestHttpClient matches query params by object equality, so check the URL the real client builds.
    @Test
    void test_sources_serializeAsWireValues() {
        final String query = httpClient.appendUrlParams(url, Map.of(
            "sources[]", Optional.of(List.of(MarketingStatsSource.CAMPAIGNS, MarketingStatsSource.AUTOMATIONS))
        ));

        assertEquals(url + "?sources[]=campaigns&sources[]=automations", query);
    }

    @Test
    void test_getMarketingStats_nullFilter() {
        assertThrows(IllegalArgumentException.class, () -> api.getMarketingStats(null));
    }

    @Test
    void test_getMarketingStats_missingStartDate() {
        final MarketingStatsFilter filter = MarketingStatsFilter.builder()
            .endDate(endDate)
            .build();

        assertThrows(IllegalArgumentException.class, () -> api.getMarketingStats(filter));
    }

    @Test
    void test_getMarketingStats_blankEndDate() {
        final MarketingStatsFilter filter = MarketingStatsFilter.builder()
            .startDate(startDate)
            .endDate(" ")
            .build();

        assertThrows(IllegalArgumentException.class, () -> api.getMarketingStats(filter));
    }
}
