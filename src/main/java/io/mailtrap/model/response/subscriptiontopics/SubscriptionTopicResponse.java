package io.mailtrap.model.response.subscriptiontopics;

import lombok.Data;

/**
 * A single subscription topic, wrapped as {@code data}.
 */
@Data
public class SubscriptionTopicResponse {

    private SubscriptionTopic data;

}
