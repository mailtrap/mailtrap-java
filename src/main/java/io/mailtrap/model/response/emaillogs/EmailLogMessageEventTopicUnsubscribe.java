package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The recipient unsubscribed from one subscription topic.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailLogMessageEventTopicUnsubscribe extends EmailLogMessageEvent {

    @JsonProperty("details")
    private EventDetailsTopicSubscription details;

    @Override
    public EmailLogEventType getEventType() {
        return EmailLogEventType.TOPIC_UNSUBSCRIBE;
    }

    @Override
    public EventDetailsTopicSubscription getDetails() {
        return details;
    }
}
