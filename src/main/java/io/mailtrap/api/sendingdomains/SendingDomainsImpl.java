package io.mailtrap.api.sendingdomains;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;
import io.mailtrap.model.request.sendingdomains.UpdateSendingDomainRequest;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;

import java.util.List;

public class SendingDomainsImpl extends ApiResource implements SendingDomains {

    public SendingDomainsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public SendingDomainsResponse create(final long accountId, final CreateSendingDomainRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/sending_domains", accountId),
            request,
            new RequestData(),
            SendingDomainsResponse.class
        );
    }

    @Override
    public List<SendingDomainsResponse> getDomains(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/sending_domains", accountId),
            new RequestData(),
            SendingDomainsResponse.class
        );
    }

    @Override
    public SendingDomainsResponse getDomain(final long accountId, final long domainId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/sending_domains/%d", accountId, domainId),
            new RequestData(),
            SendingDomainsResponse.class
        );
    }

    @Override
    public SendingDomainsResponse update(final long accountId, final long domainId, final UpdateSendingDomainRequest request) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/sending_domains/%d", accountId, domainId),
            request,
            new RequestData(),
            SendingDomainsResponse.class
        );
    }

    @Override
    public void sendDomainSetupInstructions(final long accountId, final long domainId, final SendingDomainsSetupInstructionsRequest request) {
        httpClient.post(
            String.format(apiHost + "/api/accounts/%d/sending_domains/%d/send_setup_instructions", accountId, domainId),
            request,
            new RequestData(),
            Void.class
        );
    }

    @Override
    public void deleteDomain(final long accountId, final long domainId) {
        httpClient
            .delete(
                String.format(apiHost + "/api/accounts/%d/sending_domains/%d", accountId, domainId),
                new RequestData(),
                Void.class
            );
    }
}
