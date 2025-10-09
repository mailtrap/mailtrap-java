package io.mailtrap.model.response.contactexports;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContactExportStatus {

    CREATED("created"),
    STARTED("started"),
    FINISHED("finished");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ContactExportStatus fromValue(final String value) {
        for (final ContactExportStatus status : ContactExportStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown contact export status value: " + value);
    }
}
