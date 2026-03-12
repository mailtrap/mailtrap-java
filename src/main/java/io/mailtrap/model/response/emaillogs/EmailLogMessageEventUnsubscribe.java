package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventUnsubscribe extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsUnsubscribe details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.UNSUBSCRIBE;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
