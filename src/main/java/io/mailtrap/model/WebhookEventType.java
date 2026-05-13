package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WebhookEventType {
    DELIVERY("delivery"),
    SOFT_BOUNCE("soft_bounce"),
    BOUNCE("bounce"),
    SUSPENSION("suspension"),
    UNSUBSCRIBE("unsubscribe"),
    OPEN("open"),
    SPAM_COMPLAINT("spam_complaint"),
    CLICK("click"),
    REJECT("reject");

    private final String value;

    WebhookEventType(String value) {
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
    public static WebhookEventType fromValue(String value) {
        for (WebhookEventType type : WebhookEventType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
