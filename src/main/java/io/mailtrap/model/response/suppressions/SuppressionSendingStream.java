package io.mailtrap.model.response.suppressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Sending stream as used in Suppressions API (any, transactional, or bulk).
 */
public enum SuppressionSendingStream {
    ANY("any"),
    TRANSACTIONAL("transactional"),
    BULK("bulk");

    private final String value;

    SuppressionSendingStream(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SuppressionSendingStream fromValue(String value) {
        for (SuppressionSendingStream stream : SuppressionSendingStream.values()) {
            if (stream.value.equalsIgnoreCase(value)) {
                return stream;
            }
        }
        throw new IllegalArgumentException("Unknown SuppressionSendingStream value: " + value);
    }
}
