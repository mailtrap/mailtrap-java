package io.mailtrap.model.response.subscriptiontopics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * A subscription topic. A campaign is sent for one topic, and a contact can unsubscribe from a
 * topic without unsubscribing from every email.
 */
@Data
public class SubscriptionTopic {

    private Long id;

    private String name;

    /**
     * Description of the topic, or {@code null}.
     */
    private String description;

    /**
     * ID of the sending domain the topic covers, as returned by the Domains endpoints, or
     * {@code null} when it covers every domain.
     */
    @JsonProperty("domain_id")
    private Long domainId;

    /**
     * When the topic was deleted, or {@code null} while it is active. Only a get by id returns
     * a deleted topic; the list leaves them out.
     */
    @JsonProperty("deleted_at")
    private OffsetDateTime deletedAt;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

}
