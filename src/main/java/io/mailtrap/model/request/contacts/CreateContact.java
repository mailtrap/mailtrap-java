package io.mailtrap.model.request.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class CreateContact {

    private String email;

    private Map<String, Object> fields;

    @JsonProperty("list_ids")
    private List<Long> listIds;

    /**
     * The contact's subscription topic preferences, one entry per topic and sending domain.
     * Pairs left out keep their status. The API rejects them with {@code 422} for an account
     * without subscription topics.
     */
    @JsonProperty("subscription_topics")
    private List<ContactSubscriptionTopic> subscriptionTopics;

    public CreateContact(final String email, final Map<String, Object> fields, final List<Long> listIds) {
        this(email, fields, listIds, null);
    }
}
