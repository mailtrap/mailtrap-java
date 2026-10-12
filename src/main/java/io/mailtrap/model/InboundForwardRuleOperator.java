package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InboundForwardRuleOperator {
    EQUAL("equal"),
    NOT_EQUAL("not_equal"),
    CONTAINS("contains"),
    STARTS_WITH("starts_with"),
    ENDS_WITH("ends_with"),
    EMPTY("empty"),
    NOT_EMPTY("not_empty");

    private final String value;

    InboundForwardRuleOperator(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static InboundForwardRuleOperator fromValue(String value) {
        for (InboundForwardRuleOperator operator : InboundForwardRuleOperator.values()) {
            if (operator.value.equalsIgnoreCase(value)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown InboundForwardRuleOperator value: " + value);
    }
}
