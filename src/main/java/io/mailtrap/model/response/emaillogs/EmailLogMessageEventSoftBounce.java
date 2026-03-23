package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventSoftBounce extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsBounce details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.SOFT_BOUNCE;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
