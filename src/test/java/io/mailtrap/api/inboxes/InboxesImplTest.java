package io.mailtrap.api.inboxes;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest.InboxUpdateData;
import io.mailtrap.model.response.inboxes.InboxResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.mailtrap.model.request.inboxes.CreateInboxRequest.*;
import static org.junit.jupiter.api.Assertions.*;

class InboxesImplTest extends BaseTest {

    private Inboxes api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/projects/" + projectId + "/inboxes",
                        "POST", "api/inbox/createInboxRequest.json", "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId,
                        "GET", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId,
                        "DELETE", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId,
                        "PATCH", "api/inbox/updateInboxRequest.json", "api/inbox/updateInboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId + "/clean",
                        "PATCH", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId + "/all_read",
                        "PATCH", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId + "/reset_credentials",
                        "PATCH", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId + "/toggle_email_username",
                        "PATCH", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + sandboxId + "/reset_email_username",
                        "PATCH", null, "api/inbox/inboxResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes",
                        "GET", null, "api/inbox/listInboxResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).sandboxApi().sandboxes();
    }

    @Test
    void test_createSandbox() {
        final InboxResponse createdInbox = api.createSandbox(accountId, projectId, new CreateInboxRequest(new InboxCreateData("Test inbox")));

        assertNotNull(createdInbox);
        assertEquals(createdInbox.getProjectId(), projectId);
        assertEquals("Test inbox", createdInbox.getName());
    }

    @Test
    void test_getSandboxAttributes() {
        final InboxResponse inboxAttributes = api.getSandboxAttributes(accountId, sandboxId);

        assertNotNull(inboxAttributes);
        assertTrue(inboxAttributes.getPermission().isCanRead());
        assertEquals(inboxAttributes.getProjectId(), projectId);
        assertEquals("Test inbox", inboxAttributes.getName());
    }

    @Test
    void test_deleteSandbox() {
        final InboxResponse deletedInbox = api.deleteSandbox(accountId, sandboxId);

        assertNotNull(deletedInbox);
        assertTrue(deletedInbox.getPermission().isCanRead());
        assertEquals(deletedInbox.getId(), sandboxId);
        assertEquals("Test inbox", deletedInbox.getName());
    }

    @Test
    void test_updateSandbox() {
        final InboxResponse updatedInbox = api.updateSandbox(accountId, sandboxId, new UpdateInboxRequest(new InboxUpdateData("Updated Inbox Name", "updated-email-username")));

        assertNotNull(updatedInbox);
        assertTrue(updatedInbox.getPermission().isCanRead());
        assertEquals(updatedInbox.getId(), sandboxId);
        assertEquals("Updated Inbox Name", updatedInbox.getName());
        assertEquals("updated-email-username", updatedInbox.getEmailUsername());
    }

    @Test
    void test_cleanSandbox() {
        final InboxResponse cleanedInbox = api.cleanSandbox(accountId, sandboxId);

        assertNotNull(cleanedInbox);
        assertTrue(cleanedInbox.getPermission().isCanRead());
        assertEquals(cleanedInbox.getId(), sandboxId);
        assertEquals("Test inbox", cleanedInbox.getName());
    }

    @Test
    void test_markAsRead() {
        final InboxResponse markedAsRead = api.markAsRead(accountId, sandboxId);

        assertNotNull(markedAsRead);
        assertTrue(markedAsRead.getPermission().isCanRead());
        assertEquals(markedAsRead.getId(), sandboxId);
        assertEquals("Test inbox", markedAsRead.getName());
    }

    @Test
    void test_resetCredentials() {
        final InboxResponse resetCredentials = api.resetCredentials(accountId, sandboxId);

        assertNotNull(resetCredentials);
        assertTrue(resetCredentials.getPermission().isCanRead());
        assertEquals(resetCredentials.getId(), sandboxId);
        assertEquals("Test inbox", resetCredentials.getName());
    }

    @Test
    void test_enableEmailAddress() {
        final InboxResponse enableEmailAddress = api.enableEmailAddress(accountId, sandboxId);

        assertNotNull(enableEmailAddress);
        assertTrue(enableEmailAddress.getPermission().isCanRead());
        assertEquals(enableEmailAddress.getId(), sandboxId);
        assertEquals("Test inbox", enableEmailAddress.getName());
    }

    @Test
    void test_resetEmailAddresses() {
        final InboxResponse resetEmailAddresses = api.resetEmailAddresses(accountId, sandboxId);

        assertNotNull(resetEmailAddresses);
        assertTrue(resetEmailAddresses.getPermission().isCanRead());
        assertEquals(resetEmailAddresses.getId(), sandboxId);
        assertEquals("Test inbox", resetEmailAddresses.getName());
    }

    @Test
    void test_getSandboxes() {
        final List<InboxResponse> inboxes = api.getSandboxes(accountId);

        assertNotNull(inboxes);
        assertEquals(1, inboxes.size());
        assertTrue(inboxes.get(0).getPermission().isCanRead());
        assertEquals(inboxes.get(0).getId(), sandboxId);
        assertEquals("Test inbox", inboxes.get(0).getName());
    }

}
