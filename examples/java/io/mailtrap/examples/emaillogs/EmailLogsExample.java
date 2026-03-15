package io.mailtrap.examples.emaillogs;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emaillogs.EmailLogsListFilters;
import io.mailtrap.model.request.emaillogs.FilterCiString;
import io.mailtrap.model.request.emaillogs.FilterExactString;
import io.mailtrap.model.request.emaillogs.FilterStatus;
import io.mailtrap.model.request.emaillogs.FilterOptionalString;
import io.mailtrap.model.response.emaillogs.MessageStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmailLogsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        // List email logs for the last 2 days
        final var now = Instant.now();
        final var twoDaysAgo = now.minus(2, ChronoUnit.DAYS);
        final var filters = EmailLogsListFilters.builder()
                .sentAfter(twoDaysAgo.toString())
                .sentBefore(now.toString())
                .subject(new FilterOptionalString(FilterOptionalString.Operator.not_empty))
                .to(new FilterCiString(FilterCiString.Operator.ci_equal, "recipient@example.com"))
                .category(new FilterExactString(FilterExactString.Operator.equal,
                        List.of("Newsletter", "Alert")))
                .build();

        final var listResponse = client.sendingApi().emailLogs()
                .list(ACCOUNT_ID, null, filters);

        System.out.println("Total: " + listResponse.getTotalCount());
        listResponse.getMessages().forEach(
                msg -> System.out.println("  " + msg.getMessageId() + " " + msg.getSubject() + " "
                        + msg.getStatus()));

        // Get a single message by ID (use message_id from list response)
        if (!listResponse.getMessages().isEmpty()) {
            final var messageId = listResponse.getMessages().get(0).getMessageId();
            final var message = client.sendingApi().emailLogs().get(ACCOUNT_ID, messageId);
            System.out.println(
                    "Message: " + message.getSubject() + " events: " + message.getEvents().size());
        }
    }
}
