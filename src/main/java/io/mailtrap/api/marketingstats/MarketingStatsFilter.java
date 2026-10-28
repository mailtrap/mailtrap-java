package io.mailtrap.api.marketingstats;

import io.mailtrap.model.MarketingStatsSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Filters for Email Marketing statistics. {@code startDate} and {@code endDate} are required;
 * {@code null} or empty lists are omitted from the query string.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketingStatsFilter {

    /**
     * Start of the period (inclusive), in {@code YYYY-MM-DD} format.
     */
    private String startDate;

    /**
     * End of the period (inclusive), in {@code YYYY-MM-DD} format.
     */
    private String endDate;

    /**
     * IDs of the sending domains to include, as returned by the Sending Domains API. If omitted,
     * every sending domain of the account is included.
     */
    private List<Long> sendingDomainIds;

    /**
     * Sources to include. One value narrows the results to that source; omit it, or pass both
     * values, to include campaigns and automations together.
     */
    private List<MarketingStatsSource> sources;

    /**
     * Recipient email service providers to include (e.g. {@code Google}, {@code Yahoo}). If
     * omitted, all providers are included.
     */
    private List<String> emailServiceProviders;
}
