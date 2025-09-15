package io.mailtrap.examples.sending;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.BatchEmailBase;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;
import java.util.Map;

public class Batch {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var mail = MailtrapMail.builder()
            .from(new Address(SENDER_EMAIL))
            .to(List.of(new Address(RECIPIENT_EMAIL)))
            .subject("Hello from Mailtrap Sending!")
            .text("Welcome to Mailtrap Sending!")
            .build();

        final var batchMail = MailtrapBatchMail.builder()
            // Optionally you can add this `base` object - if you have some common data across emails
            // Each property can be overridden in `requests` for individual emails
            .base(BatchEmailBase.builder().subject("Base Subject for all emails").build())
            .requests(List.of(mail))
            .build();

        System.out.println(client.sendingApi().emails().batchSend(batchMail));
    }

}
