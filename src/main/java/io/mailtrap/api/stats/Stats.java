package io.mailtrap.api.stats;

import io.mailtrap.model.response.stats.SendingStatGroupResponse;
import io.mailtrap.model.response.stats.SendingStatsResponse;

import java.util.List;

/**
 * Interface representing the Mailtrap API for interaction with sending statistics
 */
public interface Stats {

    /**
     * Get aggregated sending stats
     *
     * @param accountId unique account ID
     * @param filter    stats filter parameters
     * @return aggregated sending statistics
     */
    SendingStatsResponse getStats(long accountId, StatsFilter filter);

    /**
     * Get sending stats grouped by domains
     *
     * @param accountId unique account ID
     * @param filter    stats filter parameters
     * @return list of sending statistics grouped by domain
     */
    List<SendingStatGroupResponse> byDomains(long accountId, StatsFilter filter);

    /**
     * Get sending stats grouped by categories
     *
     * @param accountId unique account ID
     * @param filter    stats filter parameters
     * @return list of sending statistics grouped by category
     */
    List<SendingStatGroupResponse> byCategories(long accountId, StatsFilter filter);

    /**
     * Get sending stats grouped by email service providers
     *
     * @param accountId unique account ID
     * @param filter    stats filter parameters
     * @return list of sending statistics grouped by email service provider
     */
    List<SendingStatGroupResponse> byEmailServiceProviders(long accountId, StatsFilter filter);

    /**
     * Get sending stats grouped by date
     *
     * @param accountId unique account ID
     * @param filter    stats filter parameters
     * @return list of sending statistics grouped by date
     */
    List<SendingStatGroupResponse> byDate(long accountId, StatsFilter filter);
}
