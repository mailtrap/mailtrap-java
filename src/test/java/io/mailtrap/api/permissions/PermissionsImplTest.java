package io.mailtrap.api.permissions;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.permissions.ManagePermissionsRequest;
import io.mailtrap.model.request.permissions.Permission;
import io.mailtrap.model.response.permissions.ManagePermissionsResponse;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.ResourceType;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PermissionsImplTest extends BaseTest {

    private Permissions api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/account_accesses/" + accountAccessId + "/permissions/bulk",
                        "PUT", "api/permissions/updatePermissionsRequest.json", "api/permissions/updatePermissionsResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/permissions/resources",
                        "GET", null, "api/permissions/getResourcesResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).generalApi().permissions();
    }

    @Test
    void managePermissions() {
        final ManagePermissionsRequest request = new ManagePermissionsRequest(List.of(
                new Permission(String.valueOf(accountId), ResourceType.ACCOUNT, AccessLevel.VIEWER, false),
                new Permission(String.valueOf(inboxId), ResourceType.INBOX, AccessLevel.ADMIN, true)
        ));

        ManagePermissionsResponse response = api.managePermissions(accountAccessId, accountId, request);

        assertEquals("Permissions have been updated!", response.getMessage());
    }

    @Test
    void getResources() {
        final var resources = api.getResources(accountId);

        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(ResourceType.PROJECT, resources.get(0).getType());
        assertEquals(1, resources.get(0).getResources().size());
        assertEquals(ResourceType.INBOX, resources.get(0).getResources().get(0).getType());
    }
}
