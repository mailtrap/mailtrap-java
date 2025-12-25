package io.mailtrap.examples.contactlists;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactlists.CreateUpdateContactListRequest;

public class ContactListsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final String NAME_FOR_CREATE = "Clients";
    private static final String NAME_FOR_UPDATE = "Customers";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var created = client.contactsApi().contactLists().createContactList(ACCOUNT_ID, new CreateUpdateContactListRequest(NAME_FOR_CREATE));
        System.out.println(created);

        final var updated = client.contactsApi().contactLists().updateContactList(ACCOUNT_ID, created.getId(), new CreateUpdateContactListRequest(NAME_FOR_UPDATE));
        System.out.println(updated);

        final var byId = client.contactsApi().contactLists().getContactList(ACCOUNT_ID, updated.getId());
        System.out.println(byId);

        final var contactLists = client.contactsApi().contactLists().findAll(ACCOUNT_ID);
        System.out.println(contactLists);

        client.contactsApi().contactLists().deleteContactList(ACCOUNT_ID, byId.getId());
    }
}
