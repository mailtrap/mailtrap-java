package io.mailtrap.model.request.contacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Attributes used to update a contact. Build it with {@code builder()} to send only the fields
 * that are set: a field left {@code null} keeps its value.
 */
@Getter
@Builder
// Private: new fields go through the builder, so the public constructors keep their signatures
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateContact {

    private String email;

    private Map<String, Object> fields;

    @JsonProperty("list_ids_included")
    private List<Long> listIdsIncluded;

    @JsonProperty("list_ids_excluded")
    private List<Long> listIdsExcluded;

    /**
     * Whether the contact is unsubscribed from every email. Left out when {@code null}, so the
     * contact keeps its status.
     */
    @JsonProperty("unsubscribed")
    private Boolean unsubscribed;

    /**
     * The contact's subscription topic preferences, one entry per topic and sending domain.
     * Pairs left out keep their status. The API rejects them with {@code 422} for an account
     * without subscription topics.
     */
    @JsonProperty("subscription_topics")
    private List<ContactSubscriptionTopic> subscriptionTopics;

    /**
     * Sends {@code unsubscribed} as given: {@code false} resubscribes the contact.
     */
    public UpdateContact(final String email, final Map<String, Object> fields, final List<Long> listIdsIncluded,
                         final List<Long> listIdsExcluded, final boolean unsubscribed) {
        this(email, fields, listIdsIncluded, listIdsExcluded, unsubscribed, null);
    }

    /**
     * @return {@code true} only when {@code unsubscribed} is set to {@code true}
     */
    @JsonIgnore
    public boolean isUnsubscribed() {
        return Boolean.TRUE.equals(unsubscribed);
    }
}
