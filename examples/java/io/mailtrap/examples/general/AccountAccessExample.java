package io.mailtrap.examples.general;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListAccountAccessQueryParams;

import java.util.List;

public class AccountAccessExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final String INBOX_ID = "2";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        var queryParams = new ListAccountAccessQueryParams();
        queryParams.setInboxIds(List.of(INBOX_ID));

        var responses = client.generalApi().accountAccesses().listUserAndInviteAccountAccesses(ACCOUNT_ID, queryParams);

        System.out.println(responses);

        System.out.println(client.generalApi().accountAccesses().removeAccountAccess(responses.get(0).getId(), ACCOUNT_ID));
    }
}
