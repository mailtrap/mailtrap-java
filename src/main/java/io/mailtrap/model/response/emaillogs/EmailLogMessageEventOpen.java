package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventOpen extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsOpen details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.OPEN;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
