package io.mailtrap.examples.sendingdomains;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;

import static io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest.SendingDomainData;

public class SendingDomainsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final String DOMAIN_NAME = "test.io";
    private static final String DEVOPS_EMAIL = "devops@test.io";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var createRequest = new CreateSendingDomainRequest(new SendingDomainData(DOMAIN_NAME));

        final var createdDomain = client.sendingApi().domains().create(ACCOUNT_ID, createRequest);
        System.out.println(createdDomain);

        final var domainById = client.sendingApi().domains().getSendingDomain(ACCOUNT_ID, createdDomain.getId());
        System.out.println(domainById);

        final var allDomains = client.sendingApi().domains().getSendingDomains(ACCOUNT_ID);
        System.out.println(allDomains);

        final var sendInstructionsRequest = new SendingDomainsSetupInstructionsRequest(DEVOPS_EMAIL);
        client.sendingApi().domains().sendSendingDomainsSetupInstructions(ACCOUNT_ID, createdDomain.getId(), sendInstructionsRequest);

        client.sendingApi().domains().deleteSendingDomain(ACCOUNT_ID, createdDomain.getId());
    }
}
