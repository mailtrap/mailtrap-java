package io.mailtrap.examples.general;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListAccountAccessQueryParams;

import java.util.List;

public class AccountAccessExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));
    private static final String SANDBOX_ID = System.getenv("MAILTRAP_SANDBOX_ID");

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var queryParams = new ListAccountAccessQueryParams();
        queryParams.setInboxIds(List.of(SANDBOX_ID));

        final var responses = client.generalApi().accountAccesses().listUserAndInviteAccountAccesses(ACCOUNT_ID, queryParams);

        System.out.println(responses);

        System.out.println(client.generalApi().accountAccesses().removeAccountAccess(responses.get(0).getId(), ACCOUNT_ID));
    }
}
