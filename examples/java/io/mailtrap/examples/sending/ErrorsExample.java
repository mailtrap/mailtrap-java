package io.mailtrap.examples.sending;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;
import java.util.UUID;

public class ErrorsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var invalidMail_TemplateUUIDAndSubjectAreUsed = MailtrapMail.builder()
                .from(new Address(SENDER_EMAIL))
                .to(List.of(new Address(RECIPIENT_EMAIL)))
                .subject("Hello from Mailtrap Sending!")
                .text("Welcome to Mailtrap Sending!")
                .templateUuid(UUID.randomUUID().toString())
                .build();

        try {
            System.out.println(client.sendingApi().emails().send(invalidMail_TemplateUUIDAndSubjectAreUsed));
        } catch (InvalidRequestBodyException e) {
            System.out.println("Caught invalid request body exception : " + e);
        }

        var clientWithInvalidToken = MailtrapClientFactory.createMailtrapClient(new MailtrapConfig.Builder()
                .token("invalid token")
                .build());

        final var validMail = MailtrapMail.builder()
                .from(new Address(SENDER_EMAIL))
                .to(List.of(new Address(RECIPIENT_EMAIL)))
                .subject("Hello from Mailtrap Sending!")
                .text("Welcome to Mailtrap Sending!")
                .build();

        try {
            System.out.println(clientWithInvalidToken.sendingApi().emails().send(validMail));
        } catch (HttpClientException e) {
            System.out.println("Caught unauthorized exception : " + e);
        }

        try {
            System.out.println(client.sendingApi().emails().send(null));
        } catch (InvalidRequestBodyException e) {
            System.out.println("Caught invalid request body exception : " + e);
        }
    }
}

