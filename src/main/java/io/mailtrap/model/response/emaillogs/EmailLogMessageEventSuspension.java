package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventSuspension extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsReject details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.SUSPENSION;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
