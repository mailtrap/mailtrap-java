package io.mailtrap.examples.contactimports;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactimports.Contact;
import io.mailtrap.model.request.contactimports.ImportContactsRequest;
import io.mailtrap.model.request.contacts.UpdateContact;
import io.mailtrap.model.request.contacts.UpdateContactRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContactImportsExample {

  private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
  private static final long ACCOUNT_ID = 1L;
  private static final long LIST_1_ID = 1L;
  private static final long LIST_2_ID = 2L;
  private static final String EMAIL = "contact_email@email.com";

  public static void main(String[] args) {
    final var config = new MailtrapConfig.Builder()
        .token(TOKEN)
        .build();

    final var client = MailtrapClientFactory.createMailtrapClient(config);

    var importRequest = new ImportContactsRequest(
        List.of(new Contact(EMAIL, Map.of("first_name", "Nick"), List.of(LIST_1_ID), List.of(LIST_2_ID))));

    var createResponse = client.contactsApi().contactImports()
        .importContacts(ACCOUNT_ID, importRequest);

    System.out.println(createResponse);

    var contactImportResponse = client.contactsApi().contactImports()
        .getContactImport(ACCOUNT_ID, createResponse.getId());

    System.out.println(contactImportResponse);
  }
}
