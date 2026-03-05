package io.mailtrap.examples.general;

import io.mailtrap.api.stats.StatsFilter;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;

public class StatsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var filter = StatsFilter.builder()
                .startDate("2026-01-01")
                .endDate("2026-01-31")
                .build();

        System.out.println("=== Aggregated Stats ===");
        System.out.println(client.generalApi().stats().getStats(ACCOUNT_ID, filter));

        System.out.println("\n=== Stats by Domains ===");
        System.out.println(client.generalApi().stats().byDomains(ACCOUNT_ID, filter));

        System.out.println("\n=== Stats by Categories ===");
        System.out.println(client.generalApi().stats().byCategories(ACCOUNT_ID, filter));

        System.out.println("\n=== Stats by Email Service Providers ===");
        System.out.println(client.generalApi().stats().byEmailServiceProviders(ACCOUNT_ID, filter));

        System.out.println("\n=== Stats by Date ===");
        System.out.println(client.generalApi().stats().byDate(ACCOUNT_ID, filter));
    }
}
