package io.mailtrap.api.accountaccesses;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.accountaccesses.ListAccountAccessQueryParams;
import io.mailtrap.model.response.accountaccesses.*;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccountAccessesImplTest extends BaseTest {

    private AccountAccesses api;

    @BeforeEach
    public void init() {
        TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/account_accesses",
                        "GET", null, "api/account_access/getAccountAccessResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/account_accesses",
                        "GET", null, "api/account_access/getAccountAccessWithQueryParamResponse.json",
                        Map.of("project_ids", List.of(String.valueOf(projectId), String.valueOf(anotherProjectId)))),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/account_accesses/" + accountAccessId,
                        "DELETE", null, "api/account_access/removeAccountAccessResponse.json")
        ));

        MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().accountAccesses();
    }

    @Test
    void test_listUserAndInviteAccountAccesses() {
        List<AccountAccessResponse> accountAccessResponses = api.listUserAndInviteAccountAccesses(accountId, ListAccountAccessQueryParams.empty());

        assertFalse(accountAccessResponses.isEmpty());
        AccountAccessResponse accountAccessResponse = accountAccessResponses.get(0);
        assertEquals(accountAccessResponse.getId(), accountAccessId);
        assertEquals(SpecifierType.USER, accountAccessResponse.getSpecifierType());
        assertEquals("Jack Sparrow", ((UserSpecifier) accountAccessResponse.getSpecifier()).getName());
        assertTrue(((UserSpecifier) accountAccessResponse.getSpecifier()).isTwoFactorAuthenticationEnabled());
        assertEquals(3, accountAccessResponse.getResources().size());
    }

    @Test
    void test_listUserAndInviteAccountAccessesWithProjectIdsQueryParam() {
        ListAccountAccessQueryParams queryParams = new ListAccountAccessQueryParams();
        queryParams.setProjectIds(List.of(String.valueOf(projectId), String.valueOf(anotherProjectId)));

        List<AccountAccessResponse> accountAccessResponses = api.listUserAndInviteAccountAccesses(accountId, queryParams);

        assertFalse(accountAccessResponses.isEmpty());
        AccountAccessResponse accountAccessResponse = accountAccessResponses.get(0);
        assertEquals(accountAccessResponse.getId(), accountAccessId);
        assertEquals(SpecifierType.API_TOKEN, accountAccessResponse.getSpecifierType());
        assertEquals(((ApiTokenSpecifier) accountAccessResponse.getSpecifier()).getToken(), "token-value-11-22-33");
        assertEquals(accountAccessResponse.getResources().size(), 2);
        assertEquals(accountAccessResponse.getResources().get(0).getResourceId(), projectId);
    }

    @Test
    void test_removeAccountAccess() {
        RemoveAccountAccessResponse removeAccountAccessResponse = api.removeAccountAccess(accountAccessId, accountId);

        assertNotNull(removeAccountAccessResponse);
        assertEquals(removeAccountAccessResponse.getId(), accountAccessId);
    }
}
