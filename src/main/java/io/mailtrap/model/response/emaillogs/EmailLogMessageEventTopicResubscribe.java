package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The recipient resubscribed to one subscription topic.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventTopicResubscribe extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsTopicSubscription details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.TOPIC_RESUBSCRIBE;
    }
}
