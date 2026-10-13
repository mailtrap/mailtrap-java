package io.mailtrap.examples.marketingstats;

import io.mailtrap.api.marketingstats.MarketingStatsFilter;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.MarketingStatsSource;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class MarketingStatsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        // The marketing stats endpoint is token-scoped: the account is resolved from the API token.
        final var marketingStats = client.marketingApi().stats();

        // From 30 days ago through today, both inclusive (YYYY-MM-DD).
        final var today = LocalDate.now(ZoneOffset.UTC);
        final var startDate = today.minusDays(30).toString();
        final var endDate = today.toString();

        System.out.println("=== Email Marketing Stats (campaigns and automations) ===");
        System.out.println(marketingStats.getMarketingStats(
                MarketingStatsFilter.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build()).getData());

        // One source narrows the results to campaigns or to automations. Sending domains and email
        // service providers can be filtered too, with sendingDomainIds(...) and emailServiceProviders(...).
        System.out.println("\n=== Campaigns only ===");
        System.out.println(marketingStats.getMarketingStats(
                MarketingStatsFilter.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .sources(List.of(MarketingStatsSource.CAMPAIGNS))
                        .build()).getData());
    }
}
