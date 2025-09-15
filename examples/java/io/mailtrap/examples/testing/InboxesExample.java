package io.mailtrap.examples.testing;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;

public class InboxesExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var testingClient = MailtrapClientFactory.createMailtrapClient(config).testingApi();

        var projects = testingClient.projects().getProjects(ACCOUNT_ID);

        if (!projects.isEmpty()) {
            var firstProject = projects.get(0);

            var createdInbox = testingClient.inboxes().createInbox(ACCOUNT_ID, firstProject.getId(), new CreateInboxRequest(new CreateInboxRequest.InboxCreateData("test-inbox")));
            System.out.println(createdInbox);

            var inboxes = testingClient.inboxes().getInboxes(ACCOUNT_ID);

            if (!inboxes.isEmpty()) {
                long firstInboxId = inboxes.get(0).getId();

                var inboxAttributes = testingClient.inboxes().getInboxAttributes(ACCOUNT_ID, firstInboxId);
                System.out.println(inboxAttributes);

                var inboxUpdate = testingClient.inboxes().updateInbox(ACCOUNT_ID, firstInboxId, new UpdateInboxRequest(new UpdateInboxRequest.InboxUpdateData("updated-test-inbox", "mock")));
                System.out.println(inboxUpdate);

                var markAsRead = testingClient.inboxes().markAsRead(ACCOUNT_ID, firstInboxId);
                System.out.println(markAsRead);

                var cleanInbox = testingClient.inboxes().cleanInbox(ACCOUNT_ID, firstInboxId);
                System.out.println(cleanInbox);

                var resetCredentials = testingClient.inboxes().resetCredentials(ACCOUNT_ID, firstInboxId);
                System.out.println(resetCredentials);

                var enableEmailAddress = testingClient.inboxes().enableEmailAddress(ACCOUNT_ID, firstInboxId);
                System.out.println(enableEmailAddress);

                var resetEmailAddresses = testingClient.inboxes().resetEmailAddresses(ACCOUNT_ID, firstInboxId);
                System.out.println(resetEmailAddresses);
            }
        }
    }
}
