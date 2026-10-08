package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InboundForwardRuleMatchType {
    SENDER("sender"),
    RECIPIENT("recipient"),
    HEADER("header");

    private final String value;

    InboundForwardRuleMatchType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static InboundForwardRuleMatchType fromValue(String value) {
        for (InboundForwardRuleMatchType matchType : InboundForwardRuleMatchType.values()) {
            if (matchType.value.equalsIgnoreCase(value)) {
                return matchType;
            }
        }
        throw new IllegalArgumentException("Unknown InboundForwardRuleMatchType value: " + value);
    }
}
