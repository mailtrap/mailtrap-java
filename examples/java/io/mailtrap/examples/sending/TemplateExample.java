package io.mailtrap.examples.sending;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;
import java.util.Map;

public class TemplateExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var mail = MailtrapMail.builder()
                .from(new Address("John Doe", SENDER_EMAIL))
                .to(List.of(new Address("Jane Doe", RECIPIENT_EMAIL)))
                .templateUuid("813e39db-c74a-4830-b037-0e6ba8b1fe88")
                .templateVariables(Map.of(
                        "user_name", "Jack Sparrow"
                ))
                .build();

        System.out.println(client.sendingApi().emails().send(mail));
    }
}
