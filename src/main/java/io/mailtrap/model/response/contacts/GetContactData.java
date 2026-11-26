package io.mailtrap.model.response.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.request.contacts.ContactSubscriptionTopic;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class GetContactData {

    private String id;

    private String email;

    private Map<String, Object> fields;

    @JsonProperty("list_ids")
    private List<Long> listIds;

    private ContactStatus status;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    /**
     * The contact's subscription topic preferences, one entry per topic and sending domain it
     * covers. {@code null} when the account doesn't have subscription topics, or when the
     * preferences couldn't be read: then retry, rather than take the contact as subscribed.
     */
    @JsonProperty("subscription_topics")
    private List<ContactSubscriptionTopic> subscriptionTopics;

}
