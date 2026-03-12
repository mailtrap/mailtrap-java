package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of event in an email log (e.g. delivery, open, click).
 */
public enum EmailLogEventType {
    DELIVERY("delivery"),
    OPEN("open"),
    CLICK("click"),
    SOFT_BOUNCE("soft_bounce"),
    BOUNCE("bounce"),
    SPAM("spam"),
    UNSUBSCRIBE("unsubscribe"),
    SUSPENSION("suspension"),
    REJECT("reject");

    private final String value;

    EmailLogEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EmailLogEventType fromValue(String value) {
        for (EmailLogEventType type : EmailLogEventType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown EmailLogEventType value: " + value);
    }
}
