package io.mailtrap.model.response.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendingStatsResponse {

    @JsonProperty("delivery_count")
    private int deliveryCount;

    @JsonProperty("delivery_rate")
    private double deliveryRate;

    @JsonProperty("bounce_count")
    private int bounceCount;

    @JsonProperty("bounce_rate")
    private double bounceRate;

    @JsonProperty("open_count")
    private int openCount;

    @JsonProperty("open_rate")
    private double openRate;

    @JsonProperty("click_count")
    private int clickCount;

    @JsonProperty("click_rate")
    private double clickRate;

    @JsonProperty("spam_count")
    private int spamCount;

    @JsonProperty("spam_rate")
    private double spamRate;
}
