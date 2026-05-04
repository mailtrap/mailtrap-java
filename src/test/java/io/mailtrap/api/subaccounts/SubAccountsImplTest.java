package io.mailtrap.api.subaccounts;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.subaccounts.CreateSubAccountRequest;
import io.mailtrap.model.request.subaccounts.SubAccountInput;
import io.mailtrap.model.response.subaccounts.SubAccount;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubAccountsImplTest extends BaseTest {

    private final long organizationId = 1001L;

    private SubAccounts api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/organizations/" + organizationId + "/sub_accounts",
                        "GET", null, "api/subaccounts/getSubAccountsResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/organizations/" + organizationId + "/sub_accounts",
                        "POST", "api/subaccounts/createSubAccountRequest.json", "api/subaccounts/createSubAccountResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).organizationsApi().subAccounts();
    }

    @Test
    void test_getSubAccounts() {
        final List<SubAccount> subAccounts = api.getSubAccounts(organizationId);

        assertEquals(2, subAccounts.size());
        assertEquals(12345L, subAccounts.get(0).getId());
        assertEquals("Development Team Account", subAccounts.get(0).getName());
        assertEquals(12346L, subAccounts.get(1).getId());
    }

    @Test
    void test_createSubAccount() {
        final CreateSubAccountRequest request = new CreateSubAccountRequest(new SubAccountInput("New Team Account"));

        final SubAccount response = api.createSubAccount(organizationId, request);

        assertNotNull(response);
        assertEquals(12347L, response.getId());
        assertEquals("New Team Account", response.getName());
    }
}