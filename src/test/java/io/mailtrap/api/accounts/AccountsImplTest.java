package io.mailtrap.api.accounts;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.response.accounts.AccountsResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountsImplTest extends BaseTest {

    private Accounts api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts",
                        "GET", null, "api/accounts/getAllAccountsResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().accounts();
    }

    @Test
    void test_getAllAccounts() {
        final List<AccountsResponse> accounts = api.getAllAccounts();

        assertEquals(2, accounts.size());
        assertEquals(accountId, accounts.get(0).getId());
        assertTrue(accounts.get(0).getAccessLevels().contains(AccessLevel.ADMIN));
        assertEquals(anotherAccountId, accounts.get(1).getId());
        assertTrue(accounts.get(1).getAccessLevels().contains(AccessLevel.OWNER));
    }
}
