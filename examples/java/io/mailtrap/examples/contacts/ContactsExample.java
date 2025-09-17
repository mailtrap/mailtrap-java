package io.mailtrap.examples.contacts;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contacts.CreateContact;
import io.mailtrap.model.request.contacts.CreateContactRequest;
import io.mailtrap.model.request.contacts.UpdateContact;
import io.mailtrap.model.request.contacts.UpdateContactRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContactsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final long LIST_1_ID = 1L;
    private static final long LIST_2_ID = 2L;
    private static final String EMAIL = "contact_email@email.com";
    private static final boolean UNSUBSCRIBED = false;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var createRequest = new CreateContactRequest(
            new CreateContact(EMAIL, Map.of("first_name", "Nick"), List.of(LIST_1_ID, LIST_2_ID)));

        final var createResponse = client.contactsApi().contacts()
            .createContact(ACCOUNT_ID, createRequest);

        System.out.println(createResponse);

        final var updateRequest = new UpdateContactRequest(
            new UpdateContact(EMAIL, Map.of("first_name", "Nick"), List.of(LIST_1_ID, LIST_2_ID),
                Collections.emptyList(), UNSUBSCRIBED));

        final var updateResponse = client.contactsApi().contacts()
            .updateContact(ACCOUNT_ID, EMAIL, updateRequest);

        System.out.println(updateResponse);

        client.contactsApi().contacts().deleteContact(ACCOUNT_ID, EMAIL);
    }
}
