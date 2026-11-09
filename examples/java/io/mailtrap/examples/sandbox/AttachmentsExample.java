package io.mailtrap.examples.sandbox;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;

public class AttachmentsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var sandboxClient = MailtrapClientFactory.createMailtrapClient(config).sandboxApi();

        final var sandboxes = sandboxClient.sandboxes().getSandboxes(ACCOUNT_ID);

        if (!sandboxes.isEmpty()) {
            final var firstSandbox = sandboxes.get(0);

            final var messages = sandboxClient.messages().getMessages(ACCOUNT_ID, firstSandbox.getId(), ListMessagesQueryParams.empty());

            if (!messages.isEmpty()) {
                final var firstMessage = messages.get(0);

                final var attachments = sandboxClient.attachments().getAttachments(ACCOUNT_ID, firstSandbox.getId(), firstMessage.getId(), null);

                if (!attachments.isEmpty()) {
                    final var firstAttachment = attachments.get(0);

                    System.out.println(sandboxClient.attachments().getSingleAttachment(ACCOUNT_ID, firstSandbox.getId(), firstMessage.getId(), firstAttachment.getId()));
                }
            }
        }
    }
}
