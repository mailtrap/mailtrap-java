package io.mailtrap.model.request.contactimports;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.request.contacts.ContactSubscriptionTopic;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// Private: new fields go through the builder, so the public constructors keep their signatures
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Contact {

  private String email;

  private Map<String, Object> fields;

  @JsonProperty("list_ids_included")
  private List<Long> listIdsIncluded;

  @JsonProperty("list_ids_excluded")
  private List<Long> listIdsExcluded;

  /**
   * The contact's subscription topic preferences, one entry per topic and sending domain.
   * Pairs left out keep their status. The API rejects the import with {@code 422} for an
   * account without subscription topics.
   */
  @JsonProperty("subscription_topics")
  private List<ContactSubscriptionTopic> subscriptionTopics;

  public Contact(final String email, final Map<String, Object> fields, final List<Long> listIdsIncluded,
                 final List<Long> listIdsExcluded) {
    this(email, fields, listIdsIncluded, listIdsExcluded, null);
  }
}
