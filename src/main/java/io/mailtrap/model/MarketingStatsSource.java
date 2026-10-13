package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Email Marketing traffic source, used to narrow marketing statistics to campaigns or to
 * automations.
 */
public enum MarketingStatsSource {
    CAMPAIGNS("campaigns"),
    AUTOMATIONS("automations");

    private final String value;

    MarketingStatsSource(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static MarketingStatsSource fromValue(String value) {
        for (MarketingStatsSource source : MarketingStatsSource.values()) {
            if (source.value.equalsIgnoreCase(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
