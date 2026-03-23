package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of an email in the sending log.
 */
public enum MessageStatus {
    DELIVERED("delivered"),
    NOT_DELIVERED("not_delivered"),
    ENQUEUED("enqueued"),
    OPTED_OUT("opted_out");

    private final String value;

    MessageStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MessageStatus fromValue(String value) {
        for (MessageStatus status : MessageStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown MessageStatus value: " + value);
    }
}
