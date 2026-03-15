package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventSpam extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsSpam details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.SPAM;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
