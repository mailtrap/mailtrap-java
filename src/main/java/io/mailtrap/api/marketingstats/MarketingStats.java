package io.mailtrap.api.marketingstats;

import io.mailtrap.model.response.marketingstats.MarketingStatsResponse;

/**
 * Interface representing the Mailtrap API for aggregated Email Marketing statistics
 * (campaigns and automations).
 *
 * <p>This endpoint is token-scoped: the account is resolved from the API token, so the path
 * takes no account id.
 */
public interface MarketingStats {

    /**
     * Get aggregated Email Marketing statistics for a date range. Email API/SMTP traffic is not
     * included; see {@code sendingApi().stats()} for it.
     *
     * @param filter stats filter parameters; {@code startDate} and {@code endDate} are required
     * @return aggregated Email Marketing statistics
     */
    MarketingStatsResponse getMarketingStats(MarketingStatsFilter filter);
}
