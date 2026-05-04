package io.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceType {
    ACCOUNT("account"),
    BILLING("billing"),
    PROJECT("project"),
    INBOX("inbox"),
    SENDING_DOMAIN("sending_domain"),
    EMAIL_CAMPAIGN_PERMISSION_SCOPE("email_campaign_permission_scope"),
    EMAIL_TEMPLATE_PERMISSION_SCOPE("email_template_permission_scope");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ResourceType fromValue(String value) {
        for (ResourceType type : ResourceType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
