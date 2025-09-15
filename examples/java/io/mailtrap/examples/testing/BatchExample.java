package io.mailtrap.examples.testing;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.BatchEmailBase;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;
import java.util.Map;

public class BatchExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";
    private static final long INBOX_ID = 1337L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .inboxId(INBOX_ID)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var mail = MailtrapMail.builder()
            .from(new Address("John Doe", SENDER_EMAIL))
            .to(List.of(new Address("Jane Doe", RECIPIENT_EMAIL)))
            .templateUuid("813t39es-t74i-4308-b037-0n6bg8b1fe88")
            .templateVariables(Map.of(
                "user_name", "Jack Sparrow",
                "testing_template", "true"
            ))
            .build();

        final var batchMail = MailtrapBatchMail.builder()
            // Optionally you can add this `base` object - if you have some common data across emails
            // Each property can be overridden in `requests` for individual emails
            .base(BatchEmailBase.builder().templateUuid("base-template-uuid").build())
            .requests(List.of(mail))
            .build();

        System.out.println(client.testingApi().emails().batchSend(batchMail, config.getInboxId()));
    }

}
