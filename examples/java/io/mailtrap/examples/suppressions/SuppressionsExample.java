package io.mailtrap.examples.suppressions;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.SendingStream;
import io.mailtrap.model.request.suppressions.CreateSuppressionRequest;
import io.mailtrap.model.request.suppressions.SuppressionListFilter;

public class SuppressionsExample {

  private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
  private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));
  private static final long DOMAIN_ID = Long.parseLong(System.getenv("MAILTRAP_DOMAIN_ID"));
  private static final String EMAIL = "example@mailtrap.io";

  public static void main(String[] args) {
    final var config = new MailtrapConfig.Builder()
        .token(TOKEN)
        .build();

    final var client = MailtrapClientFactory.createMailtrapClient(config);
    final var suppressions = client.sendingApi().suppressions();

    // Add an email to the suppression list. Type defaults to "manual import" when omitted.
    final var created = suppressions.createSuppression(
        ACCOUNT_ID,
        CreateSuppressionRequest.builder()
            .email(EMAIL)
            .domainId(DOMAIN_ID)
            .sendingStream(SendingStream.TRANSACTIONAL)
            .build()
    );

    System.out.println(created);

    final var searchResponse = suppressions.search(ACCOUNT_ID, EMAIL);

    System.out.println(searchResponse);

    // Filter by email and creation time, and page with lastId
    System.out.println(suppressions.search(
        ACCOUNT_ID,
        SuppressionListFilter.builder()
            .email(EMAIL)
            .startTime("2025-01-01T00:00:00Z")
            .endTime("2025-12-31T23:59:59Z")
            .build()
    ));

    // Also list the contacts' subscription topic unsubscriptions, with type TOPIC_UNSUBSCRIPTION and their
    // subscriptionTopicId. Those rows can't be deleted here: a contact update resubscribes to a topic.
    System.out.println(suppressions.search(
        ACCOUNT_ID,
        SuppressionListFilter.builder()
            .email(EMAIL)
            .includeTopicUnsubscriptions(true)
            .build()
    ));

    // The default list has no topic unsubscriptions, so its first row can be deleted.
    if (!searchResponse.isEmpty()) {
      final var deletedSuppression = suppressions
          .deleteSuppression(ACCOUNT_ID, searchResponse.get(0).getId());

      System.out.println(deletedSuppression);
    }
  }
}
