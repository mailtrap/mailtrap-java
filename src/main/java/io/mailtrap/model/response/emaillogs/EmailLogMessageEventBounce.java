package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventBounce extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsBounce details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.BOUNCE;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
