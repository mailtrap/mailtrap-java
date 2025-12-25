package io.mailtrap.examples.contactevents;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactevents.CreateContactEventRequest;
import io.mailtrap.model.response.contactevents.ContactEventResponse;

import java.util.Map;

public class ContactEventsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final String CONTACT_EVENT_ID = "b691272b-3e50-4813-997b-c7c9b317dcb2";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final CreateContactEventRequest request = new CreateContactEventRequest("UserLogin", Map.of(
            "user_id", 11,
            "user_name", "Dillan Doe",
            "is_active", true
        ));

        final ContactEventResponse contactEvent = client.contactsApi().contactEvents().createContactEvent(ACCOUNT_ID, CONTACT_EVENT_ID, request);
        System.out.println(contactEvent);
    }
}
