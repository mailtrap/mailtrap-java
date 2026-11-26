package io.mailtrap.examples.contactimports;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactimports.Contact;
import io.mailtrap.model.request.contactimports.ImportContactsRequest;
import io.mailtrap.model.request.contacts.ContactSubscriptionTopic;
import io.mailtrap.model.response.contacts.ContactStatus;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContactImportsExample {

  private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
  private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));
  private static final long LIST_1_ID = 1L;
  private static final long LIST_2_ID = 2L;
  private static final String EMAIL = "contact_email@email.com";

  public static void main(String[] args) {
    final var config = new MailtrapConfig.Builder()
        .token(TOKEN)
        .build();

    final var client = MailtrapClientFactory.createMailtrapClient(config);

    // With subscription topics, each contact can carry its topic preferences, one entry per topic and sending
    // domain. An account without them rejects the import, so they are sent only when there is a topic.
    final var importRequest = new ImportContactsRequest(List.of(Contact.builder()
        .email(EMAIL)
        .fields(Map.of("first_name", "Nick"))
        .listIdsIncluded(List.of(LIST_1_ID))
        .listIdsExcluded(List.of(LIST_2_ID))
        .subscriptionTopics(firstTopicPreference(client, ContactStatus.UNSUBSCRIBED).map(List::of).orElse(null))
        .build()));

    final var createResponse = client.contactsApi().contactImports()
        .importContacts(ACCOUNT_ID, importRequest);

    System.out.println(createResponse);

    final var contactImportResponse = client.contactsApi().contactImports()
        .getContactImport(ACCOUNT_ID, createResponse.getId());

    System.out.println(contactImportResponse);
  }

  /**
   * The first subscription topic, as an entry on its sending domain or on the account's first one; empty for an
   * account without subscription topics.
   */
  private static Optional<ContactSubscriptionTopic> firstTopicPreference(final MailtrapClient client,
                                                                         final ContactStatus status) {
    final List<SubscriptionTopic> topics;
    try {
      topics = client.marketingApi().subscriptionTopics().getSubscriptionTopics(null).getData();
    } catch (final HttpClientException e) {
      System.out.println("Subscription topics aren't available for this account: " + e.getMessage());
      return Optional.empty();
    }
    if (topics.isEmpty()) {
      return Optional.empty();
    }

    final var topic = topics.get(0);
    // A topic without a domain covers every sending domain, and an entry names one of them.
    final Optional<Long> domainId = topic.getDomainId() != null
        ? Optional.of(topic.getDomainId())
        : client.sendingApi().domains().getDomains(ACCOUNT_ID).stream().findFirst().map(SendingDomainsResponse::getId);

    return domainId.map(id -> ContactSubscriptionTopic.builder()
        .subscriptionTopicId(topic.getId())
        .domainId(id)
        .status(status)
        .build());
  }
}
