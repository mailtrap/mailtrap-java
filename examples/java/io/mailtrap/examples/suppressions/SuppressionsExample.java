package io.mailtrap.examples.suppressions;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;

public class SuppressionsExample {

  private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
  private static final long ACCOUNT_ID = 1L;
  private static final String EMAIL = "example@mailtrap.io";

  public static void main(String[] args) {
    final var config = new MailtrapConfig.Builder()
        .token(TOKEN)
        .build();

    final var client = MailtrapClientFactory.createMailtrapClient(config);

    final var searchResponse = client.sendingApi().suppressions()
        .search(ACCOUNT_ID, EMAIL);

    System.out.println(searchResponse);

    if (!searchResponse.isEmpty()) {
      final var deletedSuppression = client.sendingApi().suppressions()
          .deleteSuppression(ACCOUNT_ID, searchResponse.get(0).getId());

      System.out.println(deletedSuppression);
    }
  }
}
