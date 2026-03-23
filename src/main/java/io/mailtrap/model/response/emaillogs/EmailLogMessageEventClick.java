package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventClick extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsClick details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.CLICK;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
