package io.mailtrap.examples.contactfields;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.ContactFieldDataType;
import io.mailtrap.model.request.contactfields.CreateContactFieldRequest;
import io.mailtrap.model.request.contactfields.UpdateContactFieldRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContactFieldsExample {

  private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
  private static final long ACCOUNT_ID = 1L;

  public static void main(String[] args) {
    final var config = new MailtrapConfig.Builder()
        .token(TOKEN)
        .build();

    final var client = MailtrapClientFactory.createMailtrapClient(config);

    final var createResponse = client.contactsApi().contactFields()
        .createContactField(ACCOUNT_ID, new CreateContactFieldRequest("Contact name", ContactFieldDataType.TEXT, "merge-tag"));

    System.out.println(createResponse);

    final var updateResponse = client.contactsApi().contactFields()
        .updateContactField(ACCOUNT_ID, createResponse.getId(), new UpdateContactFieldRequest("Updated name", "updated-merge-tag"));

    System.out.println(updateResponse);

    final var allContactFields = client.contactsApi().contactFields()
        .getAllContactFields(ACCOUNT_ID);

    System.out.println(allContactFields);

    final var contactField = client.contactsApi().contactFields()
        .getContactField(ACCOUNT_ID, createResponse.getId());

    System.out.println(contactField);

    client.contactsApi().contactFields()
        .deleteContactField(ACCOUNT_ID, createResponse.getId());
  }
}
