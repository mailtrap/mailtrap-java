package io.mailtrap.examples.organizations;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.subaccounts.CreateSubAccountRequest;
import io.mailtrap.model.request.subaccounts.SubAccountInput;

public class SubAccountsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ORGANIZATION_ID = Long.parseLong(System.getenv("MAILTRAP_ORGANIZATION_ID"));
    private static final String SUB_ACCOUNT_NAME = "Acme Marketing";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        // Requires sub account management permissions for the organization.
        final var allSubAccounts = client.organizationsApi().subAccounts().getSubAccounts(ORGANIZATION_ID);
        System.out.println(allSubAccounts);

        final var createRequest = new CreateSubAccountRequest(new SubAccountInput(SUB_ACCOUNT_NAME));
        final var createdSubAccount = client.organizationsApi().subAccounts()
            .createSubAccount(ORGANIZATION_ID, createRequest);
        System.out.println(createdSubAccount);

        // Permanent – removes all sub account data. Deleting the last sub account deletes the organization.
        client.organizationsApi().subAccounts().deleteSubAccount(ORGANIZATION_ID, createdSubAccount.getId());
        System.out.println("Sub account " + createdSubAccount.getId() + " deleted");
    }
}