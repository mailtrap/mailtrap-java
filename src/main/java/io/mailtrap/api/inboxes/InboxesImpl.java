package io.mailtrap.api.inboxes;

import io.mailtrap.Constants;
import io.mailtrap.MailtrapValidator;
import io.mailtrap.api.apiresource.ApiResourceWithValidation;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;
import io.mailtrap.model.response.inboxes.InboxResponse;

import java.util.List;

public class InboxesImpl extends ApiResourceWithValidation implements Inboxes {

    public InboxesImpl(final MailtrapConfig config, final MailtrapValidator validator) {
        super(config, validator);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public InboxResponse createSandbox(final long accountId, final long projectId, final CreateInboxRequest request) {

        validateRequestBodyAndThrowException(request);

        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/projects/%d/inboxes", accountId, projectId),
            request,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse getSandboxAttributes(final long accountId, final long sandboxId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d", accountId, sandboxId),
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse deleteSandbox(final long accountId, final long sandboxId) {
        return httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d", accountId, sandboxId),
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse updateSandbox(final long accountId, final long sandboxId, final UpdateInboxRequest request) {

        validateRequestBodyAndThrowException(request);

        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d", accountId, sandboxId),
            request,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse cleanSandbox(final long accountId, final long sandboxId) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/clean", accountId, sandboxId),
            null,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse markAsRead(final long accountId, final long sandboxId) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/all_read", accountId, sandboxId),
            null,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse resetCredentials(final long accountId, final long sandboxId) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/reset_credentials", accountId, sandboxId),
            null,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse enableEmailAddress(final long accountId, final long sandboxId) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/toggle_email_username", accountId, sandboxId),
            null,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public InboxResponse resetEmailAddresses(final long accountId, final long sandboxId) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/reset_email_username", accountId, sandboxId),
            null,
            new RequestData(),
            InboxResponse.class
        );
    }

    @Override
    public List<InboxResponse> getSandboxes(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/inboxes", accountId),
            new RequestData(),
            InboxResponse.class);
    }
}
