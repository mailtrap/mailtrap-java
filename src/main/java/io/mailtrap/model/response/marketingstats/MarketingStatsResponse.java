package io.mailtrap.model.response.marketingstats;

import lombok.Data;

/**
 * Aggregated Email Marketing statistics wrapped in the {@code data} envelope.
 */
@Data
public class MarketingStatsResponse {

    private MarketingStatsData data;

}
