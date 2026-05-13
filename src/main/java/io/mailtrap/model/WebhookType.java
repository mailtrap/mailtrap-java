package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WebhookType {
    EMAIL_SENDING("email_sending"),
    AUDIT_LOG("audit_log"),
    CAMPAIGNS("campaigns");

    private final String value;

    WebhookType(String value) {
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
    public static WebhookType fromValue(String value) {
        for (WebhookType type : WebhookType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
