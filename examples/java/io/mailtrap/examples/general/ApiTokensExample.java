package io.mailtrap.examples.general;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.ResourceType;
import io.mailtrap.model.request.apitokens.ApiTokenResource;
import io.mailtrap.model.request.apitokens.CreateApiTokenRequest;

import java.util.List;

public class ApiTokensExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        // The full token value is returned only on creation — store it securely.
        final var createRequest = new CreateApiTokenRequest(
            "My token",
            List.of(new ApiTokenResource(ResourceType.ACCOUNT, ACCOUNT_ID, AccessLevel.VIEWER)));

        final var createdToken = client.generalApi().apiTokens()
            .createApiToken(ACCOUNT_ID, createRequest);
        System.out.println(createdToken);

        final var tokenId = createdToken.getId();

        final var allTokens = client.generalApi().apiTokens().getAllApiTokens(ACCOUNT_ID);
        System.out.println(allTokens);

        final var token = client.generalApi().apiTokens().getApiToken(ACCOUNT_ID, tokenId);
        System.out.println(token);

        // Reset expires the existing token and returns a new one with the same permissions.
        // The new token value is only returned here.
        final var resetToken = client.generalApi().apiTokens().resetApiToken(ACCOUNT_ID, tokenId);
        System.out.println(resetToken);

        client.generalApi().apiTokens().deleteApiToken(ACCOUNT_ID, resetToken.getId());
    }
}
