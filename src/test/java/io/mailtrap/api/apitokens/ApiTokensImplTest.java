package io.mailtrap.api.apitokens;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.ResourceType;
import io.mailtrap.model.request.apitokens.ApiTokenResource;
import io.mailtrap.model.request.apitokens.CreateApiTokenRequest;
import io.mailtrap.model.response.apitokens.ApiToken;
import io.mailtrap.model.response.apitokens.ApiTokenWithToken;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiTokensImplTest extends BaseTest {

    private final long apiTokenId = 12345L;

    private ApiTokens api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/api_tokens",
                        "GET", null, "api/apitokens/listApiTokensResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/api_tokens",
                        "POST", "api/apitokens/createApiTokenRequest.json", "api/apitokens/createApiTokenResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/api_tokens/" + apiTokenId,
                        "GET", null, "api/apitokens/getApiTokenResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/api_tokens/" + apiTokenId,
                        "DELETE", null, null),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/api_tokens/" + apiTokenId + "/reset",
                        "POST", null, "api/apitokens/resetApiTokenResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().apiTokens();
    }

    @Test
    void test_getAllApiTokens() {
        final List<ApiToken> tokens = api.getAllApiTokens(accountId);

        assertEquals(2, tokens.size());
        assertEquals(12345L, tokens.get(0).getId());
        assertEquals("x7k9", tokens.get(0).getLast4Digits());
        assertEquals(ResourceType.ACCOUNT, tokens.get(0).getResources().get(0).getResourceType());
        assertEquals(AccessLevel.ADMIN, tokens.get(0).getResources().get(0).getAccessLevel());
        assertEquals(ResourceType.INBOX, tokens.get(1).getResources().get(0).getResourceType());
        assertEquals(AccessLevel.VIEWER, tokens.get(1).getResources().get(0).getAccessLevel());
        assertNotNull(tokens.get(1).getExpiresAt());
    }

    @Test
    void test_createApiToken() {
        final CreateApiTokenRequest request = new CreateApiTokenRequest(
                "Scratch test token",
                List.of(new ApiTokenResource(ResourceType.ACCOUNT, accountId, AccessLevel.ADMIN))
        );

        final ApiTokenWithToken response = api.createApiToken(accountId, request);

        assertNotNull(response);
        assertEquals(12345L, response.getId());
        assertEquals("a1b2c3d4e5f6", response.getToken());
        assertEquals(AccessLevel.ADMIN, response.getResources().get(0).getAccessLevel());
        assertNull(response.getExpiresAt());
    }

    @Test
    void test_getApiToken() {
        final ApiToken token = api.getApiToken(accountId, apiTokenId);

        assertNotNull(token);
        assertEquals(apiTokenId, token.getId());
        assertEquals("My API Token", token.getName());
        assertEquals(ResourceType.ACCOUNT, token.getResources().get(0).getResourceType());
    }

    @Test
    void test_deleteApiToken() {
        api.deleteApiToken(accountId, apiTokenId);
    }

    @Test
    void test_resetApiToken() {
        final ApiTokenWithToken response = api.resetApiToken(accountId, apiTokenId);

        assertNotNull(response);
        assertEquals(apiTokenId, response.getId());
        assertEquals("newtoken123", response.getToken());
        assertEquals("n3w0", response.getLast4Digits());
    }
}
