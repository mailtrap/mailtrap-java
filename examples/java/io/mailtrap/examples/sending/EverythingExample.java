package io.mailtrap.examples.sending;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.EmailAttachment;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class EverythingExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";
    private static final String REPLY_TO_EMAIL = "reply_to@domain.com";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final String welcomeImage = readAndEncodeAttachment("welcome.png");

        final var mail = MailtrapMail.builder()
                .category("test")
                .customVariables(Map.of(
                        "hello", "world",
                        "year", "2024",
                        "anticipated", "true"
                ))
                .from(new Address(SENDER_EMAIL))
                .to(List.of(new Address(RECIPIENT_EMAIL)))
                .replyTo(new Address(REPLY_TO_EMAIL, "Reply To"))
                .subject("Hello from Mailtrap!")
                .html("""
                        <!doctype html>
                        <html>
                          <head>
                            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                          </head>
                          <body style="font-family: sans-serif;">
                            <div style="display: block; margin: auto; max-width: 600px;" class="main">
                              <h1 style="font-size: 18px; font-weight: bold; margin-top: 20px">Congrats for sending test email with Mailtrap!</h1>
                              <p>Inspect it using the tabs you see above and learn how this email can be improved.</p>
                              <img alt="Inspect with Tabs" src="cid:welcome.png" style="width: 100%;">
                              <p>Now send your email using our fake SMTP server and integration of your choice!</p>
                              <p>Good luck! Hope it works.</p>
                            </div>
                            <!-- Example of invalid for email html/css, will be detected by Mailtrap: -->
                            <style>
                              .main { background-color: white; }
                              a:hover { border-left-width: 1em; min-height: 2em; }
                            </style>
                          </body>
                        </html>
                        """)
                .attachments(List.of(EmailAttachment.builder()
                        .filename("welcome.png")
                        .contentId("welcome.png")
                        .disposition("inline")
                        .content(welcomeImage)
                        .build()))
                .build();

        System.out.println(client.sendingApi().emails().send(mail));
    }

    private static String readAndEncodeAttachment(String filename) {
        try (final InputStream inputStream = Everything.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                return "";
            }
            return Base64.getEncoder().encodeToString(inputStream.readAllBytes());
        } catch (final Exception e) {
            return "";
        }

    }
}
