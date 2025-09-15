package io.mailtrap.api.billing;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.billing.BillingResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BillingImplTest extends BaseTest {
    private Billing api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/billing/usage",
                        "GET", null, "api/billing/currentBillingCycleUsageResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().billing();
    }

    @Test
    void getCurrentBillingCycleUsage() {
        final BillingResponse billingResponse = api.getCurrentBillingCycleUsage(accountId);

        assertNotNull(billingResponse);
        assertEquals("Individual", billingResponse.getTestingBillingInfo().getPlan().getName());
        assertEquals("Basic 10K", billingResponse.getSendingBillingInfo().getPlan().getName());
    }
}
