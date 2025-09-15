package io.mailtrap.examples.general;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.ResourceType;
import io.mailtrap.model.request.permissions.ManagePermissionsRequest;
import io.mailtrap.model.request.permissions.Permission;

import java.util.List;

public class PermissionsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final long ACCOUNT_ACCESS_ID = 3L;

    private static final String FIRST_RESOURCE_ID = "12";
    private static final String SECOND_RESOURCE_ID = "21";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        var firstResource = client.generalApi().permissions().getResources(ACCOUNT_ID).get(0);
        System.out.println(firstResource);

        var request = new ManagePermissionsRequest(List.of(
                new Permission(
                        FIRST_RESOURCE_ID, ResourceType.ACCOUNT, AccessLevel.VIEWER, false
                ),
                new Permission(
                        SECOND_RESOURCE_ID, ResourceType.INBOX, AccessLevel.ADMIN, true
                )
        ));

        System.out.println(client.generalApi().permissions().managePermissions(ACCOUNT_ACCESS_ID, ACCOUNT_ID, request));
    }
}
