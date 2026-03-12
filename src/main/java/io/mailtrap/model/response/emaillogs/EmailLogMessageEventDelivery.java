package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventDelivery extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsDelivery details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.DELIVERY;
    }

    @Override
    public Object getDetails() {
        return details;
    }
}
