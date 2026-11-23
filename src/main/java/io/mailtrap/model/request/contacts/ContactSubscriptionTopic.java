package io.mailtrap.model.request.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.response.contacts.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A contact's subscription status for one subscription topic on one sending domain. Used both
 * in contact create, update and import requests and in contact responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSubscriptionTopic {

    /**
     * ID of the subscription topic, as returned by the Subscription Topics API.
     */
    @JsonProperty("subscription_topic_id")
    private Long subscriptionTopicId;

    /**
     * ID of the sending domain, as returned by the Domains endpoints. Required in requests; the
     * topic must cover the domain.
     */
    @JsonProperty("domain_id")
    private Long domainId;

    private ContactStatus status;

}
