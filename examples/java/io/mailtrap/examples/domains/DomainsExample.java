package io.mailtrap.examples.domains;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;
import io.mailtrap.model.request.sendingdomains.UpdateSendingDomainRequest;

import static io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest.SendingDomainData;

public class DomainsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));
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

        final var domainById = client.sendingApi().domains().getDomain(ACCOUNT_ID, createdDomain.getId());
        System.out.println(domainById);

        final var allDomains = client.sendingApi().domains().getDomains(ACCOUNT_ID);
        System.out.println(allDomains);

        final var updateRequest = new UpdateSendingDomainRequest(
            UpdateSendingDomainRequest.SendingDomainData.builder()
                .openTrackingEnabled(true)
                .clickTrackingEnabled(true)
                .trackingOptOutEnabled(true)
                .autoUnsubscribeLinkEnabled(false)
                .build()
        );

        final var updatedDomain = client.sendingApi().domains().update(ACCOUNT_ID, createdDomain.getId(), updateRequest);
        System.out.println(updatedDomain);

        final var sendInstructionsRequest = new SendingDomainsSetupInstructionsRequest(DEVOPS_EMAIL);
        client.sendingApi().domains().sendDomainsSetupInstructions(ACCOUNT_ID, createdDomain.getId(), sendInstructionsRequest);

        client.sendingApi().domains().deleteDomain(ACCOUNT_ID, createdDomain.getId());
    }
}
