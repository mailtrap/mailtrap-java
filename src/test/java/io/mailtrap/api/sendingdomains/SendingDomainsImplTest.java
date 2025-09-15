package io.mailtrap.api.sendingdomains;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendingDomainsImplTest extends BaseTest {

    private SendingDomains domains;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(
                Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/sending_domains",
                "POST", "api/sending_domains/createSendingDomainRequest.json", "api/sending_domains/sendingDomainResponse.json"
            ),
            DataMock.build(
                Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/sending_domains",
                "GET", null, "api/sending_domains/sendingDomainsResponse.json"
            ),
            DataMock.build(
                Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/sending_domains/" + sendingDomainId,
                "GET", null, "api/sending_domains/sendingDomainResponse.json"
            ),
            DataMock.build(
                Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/sending_domains/" + sendingDomainId + "/send_setup_instructions",
                "POST", "api/sending_domains/sendSetupInstructionsRequest.json", null
            ),
            DataMock.build(
                Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/sending_domains/" + sendingDomainId,
                "DELETE", null, null
            )
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        domains = MailtrapClientFactory.createMailtrapClient(testConfig).sendingApi().domains();
    }

    @Test
    void test_create() {
        final SendingDomainsResponse response = domains.create(accountId, new CreateSendingDomainRequest(new CreateSendingDomainRequest.SendingDomainData("test.io")));

        assertNotNull(response);
        assertEquals("test.io", response.getDomainName());
        assertEquals(6, response.getDnsRecords().size());
    }

    @Test
    void test_getSendingDomains() {
        final List<SendingDomainsResponse> response = domains.getSendingDomains(accountId);

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("test.io", response.get(0).getDomainName());
        assertEquals(6, response.get(0).getDnsRecords().size());

    }

    @Test
    void test_getSendingDomain() {
        final SendingDomainsResponse response = domains.getSendingDomain(accountId, sendingDomainId);

        assertNotNull(response);
        assertEquals("test.io", response.getDomainName());
        assertEquals(6, response.getDnsRecords().size());
    }

    @Test
    void test_sendSendingDomainsSetupInstructions() {
        domains.sendSendingDomainsSetupInstructions(accountId, sendingDomainId, new SendingDomainsSetupInstructionsRequest("devops@test.io"));
    }

    @Test
    void test_deleteSendingDomain() {
        assertDoesNotThrow(() -> domains.deleteSendingDomain(accountId, sendingDomainId));
    }
}
