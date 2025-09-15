package io.mailtrap.examples.general;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;

public class AccountsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        System.out.println(client.generalApi().accounts().getAllAccounts());
    }
}
