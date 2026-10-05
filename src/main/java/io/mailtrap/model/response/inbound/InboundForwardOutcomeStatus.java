package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InboundForwardOutcomeStatus {
    FORWARDED("forwarded"),
    REJECTED("rejected");

    private final String value;

    InboundForwardOutcomeStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static InboundForwardOutcomeStatus fromValue(String value) {
        for (InboundForwardOutcomeStatus status : InboundForwardOutcomeStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown InboundForwardOutcomeStatus value: " + value);
    }
}
