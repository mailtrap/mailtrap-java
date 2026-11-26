package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Details of a {@code topic_unsubscribe} or {@code topic_resubscribe} event.
 */
@Data
public class EventDetailsTopicSubscription {

    @JsonProperty("web_ip_address")
    private String webIpAddress;

    /**
     * ID of the subscription topic, as returned by the Subscription Topics API.
     */
    @JsonProperty("subscription_topic_id")
    private Long subscriptionTopicId;
}
