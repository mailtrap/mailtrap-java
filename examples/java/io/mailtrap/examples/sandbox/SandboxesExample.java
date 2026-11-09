package io.mailtrap.examples.sandbox;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;

public class SandboxesExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var sandboxClient = MailtrapClientFactory.createMailtrapClient(config).sandboxApi();

        final var projects = sandboxClient.projects().getProjects(ACCOUNT_ID);

        if (!projects.isEmpty()) {
            final var firstProject = projects.get(0);

            final var createdSandbox = sandboxClient.sandboxes().createSandbox(ACCOUNT_ID, firstProject.getId(), new CreateInboxRequest(new CreateInboxRequest.InboxCreateData("test-sandbox")));
            System.out.println(createdSandbox);

            final var sandboxes = sandboxClient.sandboxes().getSandboxes(ACCOUNT_ID);

            if (!sandboxes.isEmpty()) {
                long firstSandboxId = sandboxes.get(0).getId();

                final var sandboxAttributes = sandboxClient.sandboxes().getSandboxAttributes(ACCOUNT_ID, firstSandboxId);
                System.out.println(sandboxAttributes);

                final var sandboxUpdate = sandboxClient.sandboxes().updateSandbox(ACCOUNT_ID, firstSandboxId, new UpdateInboxRequest(new UpdateInboxRequest.InboxUpdateData("updated-test-sandbox", "mock")));
                System.out.println(sandboxUpdate);

                final var markAsRead = sandboxClient.sandboxes().markAsRead(ACCOUNT_ID, firstSandboxId);
                System.out.println(markAsRead);

                final var cleanSandbox = sandboxClient.sandboxes().cleanSandbox(ACCOUNT_ID, firstSandboxId);
                System.out.println(cleanSandbox);

                final var resetCredentials = sandboxClient.sandboxes().resetCredentials(ACCOUNT_ID, firstSandboxId);
                System.out.println(resetCredentials);

                final var enableEmailAddress = sandboxClient.sandboxes().enableEmailAddress(ACCOUNT_ID, firstSandboxId);
                System.out.println(enableEmailAddress);

                final var resetEmailAddresses = sandboxClient.sandboxes().resetEmailAddresses(ACCOUNT_ID, firstSandboxId);
                System.out.println(resetEmailAddresses);
            }
        }
    }
}
