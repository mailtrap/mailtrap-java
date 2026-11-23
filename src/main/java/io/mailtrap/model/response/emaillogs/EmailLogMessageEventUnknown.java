package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * An event of a type this SDK version doesn't know, such as one added to the API later. It
 * keeps the raw type and details, so reading the message doesn't fail.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventUnknown extends EmailLogMessageEvent {

    /**
     * The event type as the API sent it.
     */
    @JsonProperty("event_type")
    private String rawEventType;

    @JsonProperty("details")
    private Map<String, Object> details;

    /**
     * @return {@code null}, since the type isn't one of {@link EmailLogEventType}; see
     * {@code getRawEventType()}
     */
    @Override
    public EmailLogEventType getEventType() {
        return null;
    }

    @Override
    public Map<String, Object> getDetails() {
        return details;
    }
}
