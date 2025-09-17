package io.mailtrap.examples.testing;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;

public class AttachmentsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var testingClient = MailtrapClientFactory.createMailtrapClient(config).testingApi();

        final var inboxes = testingClient.inboxes().getInboxes(ACCOUNT_ID);

        if (!inboxes.isEmpty()) {
            final var firstInbox = inboxes.get(0);

            final var messages = testingClient.messages().getMessages(ACCOUNT_ID, firstInbox.getId(), ListMessagesQueryParams.empty());

            if (!messages.isEmpty()) {
                final var firstMessage = messages.get(0);

                final var attachments = testingClient.attachments().getAttachments(ACCOUNT_ID, firstInbox.getId(), firstMessage.getId(), null);

                if (!attachments.isEmpty()) {
                    final var firstAttachment = attachments.get(0);

                    System.out.println(testingClient.attachments().getSingleAttachment(ACCOUNT_ID, firstInbox.getId(), firstMessage.getId(), firstAttachment.getId()));
                }
            }
        }
    }
}
