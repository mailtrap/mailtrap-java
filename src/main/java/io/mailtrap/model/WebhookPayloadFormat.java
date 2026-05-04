package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WebhookPayloadFormat {
    JSON("json"),
    JSONLINES("jsonlines");

    private final String value;

    WebhookPayloadFormat(String value) {
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
    public static WebhookPayloadFormat fromValue(String value) {
        for (WebhookPayloadFormat format : WebhookPayloadFormat.values()) {
            if (format.value.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
