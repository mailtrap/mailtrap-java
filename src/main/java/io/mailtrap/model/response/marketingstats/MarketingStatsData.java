package io.mailtrap.model.response.marketingstats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Aggregated Email Marketing metrics for the requested period and filters. Rates are fractions
 * from 0 to 1; delivery and bounce rates are shares of {@code sentCount}, the others shares of
 * {@code deliveryCount} (per-campaign statistics divide every rate by {@code sentCount}). Every
 * count and rate is {@code 0} when nothing matched.
 */
@Data
public class MarketingStatsData {

    @JsonProperty("delivery_count")
    private Integer deliveryCount;

    @JsonProperty("delivery_rate")
    private Double deliveryRate;

    @JsonProperty("bounce_count")
    private Integer bounceCount;

    @JsonProperty("bounce_rate")
    private Double bounceRate;

    @JsonProperty("open_count")
    private Integer openCount;

    @JsonProperty("open_rate")
    private Double openRate;

    @JsonProperty("click_count")
    private Integer clickCount;

    @JsonProperty("click_rate")
    private Double clickRate;

    @JsonProperty("spam_count")
    private Integer spamCount;

    @JsonProperty("spam_rate")
    private Double spamRate;

    @JsonProperty("unsubscription_count")
    private Integer unsubscriptionCount;

    @JsonProperty("unsubscription_rate")
    private Double unsubscriptionRate;

    /**
     * Delivered plus bounced messages.
     */
    @JsonProperty("sent_count")
    private Integer sentCount;

}
