package io.mailtrap.examples.testing;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;
import io.mailtrap.model.request.messages.ForwardMessageRequest;
import io.mailtrap.model.request.messages.UpdateMessageRequest;

public class MessagesExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String FORWARD_EMAIL_TO = "recipient@domain.com";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var testingClient = MailtrapClientFactory.createMailtrapClient(config).testingApi();

        final var inboxes = testingClient.inboxes().getInboxes(ACCOUNT_ID);

        if (!inboxes.isEmpty()) {
            long firstInboxId = inboxes.get(0).getId();

            final var messages = testingClient.messages().getMessages(ACCOUNT_ID, firstInboxId, ListMessagesQueryParams.empty());

            if (!messages.isEmpty()) {
                long firstMessageId = messages.get(0).getId();

                final var getMessage = testingClient.messages().getMessage(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(getMessage);

                final var htmlAnalysis = testingClient.messages().getMessageHtmlAnalysis(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(htmlAnalysis);

                String htmlMessage = testingClient.messages().getHtmlMessage(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(htmlMessage);

                String textMessage = testingClient.messages().getTextMessage(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(textMessage);

                String rawMessage = testingClient.messages().getRawMessage(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(rawMessage);

                String messageAsEml = testingClient.messages().getMessageAsEml(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(messageAsEml);

                String messageSource = testingClient.messages().getMessageSource(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(messageSource);

                final var mailHeaders = testingClient.messages().getMailHeaders(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(mailHeaders);

                final var spamScore = testingClient.messages().getSpamScore(ACCOUNT_ID, firstInboxId, firstMessageId);
                System.out.println(spamScore);

                final var updateMessage = testingClient.messages().updateMessage(ACCOUNT_ID, firstInboxId, firstMessageId, new UpdateMessageRequest(new UpdateMessageRequest.MessageUpdateData("true")));
                System.out.println(updateMessage);

                final var forwardMessage = testingClient.messages().forwardMessage(ACCOUNT_ID, firstInboxId, firstMessageId, new ForwardMessageRequest(FORWARD_EMAIL_TO));
                System.out.println(forwardMessage);
            }
        }
    }
}
