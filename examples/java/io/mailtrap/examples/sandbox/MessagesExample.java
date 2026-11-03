package io.mailtrap.examples.sandbox;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;
import io.mailtrap.model.request.messages.ForwardMessageRequest;
import io.mailtrap.model.request.messages.UpdateMessageRequest;

public class MessagesExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final String FORWARD_EMAIL_TO = "recipient@domain.com";
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var sandboxClient = MailtrapClientFactory.createMailtrapClient(config).sandboxApi();

        final var sandboxes = sandboxClient.sandboxes().getSandboxes(ACCOUNT_ID);

        if (!sandboxes.isEmpty()) {
            long firstSandboxId = sandboxes.get(0).getId();

            final var messages = sandboxClient.messages().getMessages(ACCOUNT_ID, firstSandboxId, ListMessagesQueryParams.empty());

            if (!messages.isEmpty()) {
                long firstMessageId = messages.get(0).getId();

                final var getMessage = sandboxClient.messages().getMessage(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(getMessage);

                final var htmlAnalysis = sandboxClient.messages().getMessageHtmlAnalysis(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(htmlAnalysis);

                String htmlMessage = sandboxClient.messages().getHtmlMessage(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(htmlMessage);

                String textMessage = sandboxClient.messages().getTextMessage(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(textMessage);

                String rawMessage = sandboxClient.messages().getRawMessage(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(rawMessage);

                String messageAsEml = sandboxClient.messages().getMessageAsEml(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(messageAsEml);

                String messageSource = sandboxClient.messages().getMessageSource(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(messageSource);

                final var mailHeaders = sandboxClient.messages().getMailHeaders(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(mailHeaders);

                final var spamScore = sandboxClient.messages().getSpamScore(ACCOUNT_ID, firstSandboxId, firstMessageId);
                System.out.println(spamScore);

                final var updateMessage = sandboxClient.messages().updateMessage(ACCOUNT_ID, firstSandboxId, firstMessageId, new UpdateMessageRequest(new UpdateMessageRequest.MessageUpdateData("true")));
                System.out.println(updateMessage);

                final var forwardMessage = sandboxClient.messages().forwardMessage(ACCOUNT_ID, firstSandboxId, firstMessageId, new ForwardMessageRequest(FORWARD_EMAIL_TO));
                System.out.println(forwardMessage);
            }
        }
    }
}
