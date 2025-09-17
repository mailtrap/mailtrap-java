package io.mailtrap.api.accountaccesses;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.accountaccesses.ListAccountAccessQueryParams;
import io.mailtrap.model.response.accountaccesses.AccountAccessResponse;
import io.mailtrap.model.response.accountaccesses.RemoveAccountAccessResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class AccountAccessesImpl extends ApiResource implements AccountAccesses {

    public AccountAccessesImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public List<AccountAccessResponse> listUserAndInviteAccountAccesses(final long accountId, @NonNull final ListAccountAccessQueryParams params) {
        final var queryParams = RequestData.buildQueryParams(
            entry("domain_uuids", Optional.ofNullable(params.getDomainUuids())),
            entry("inbox_ids", Optional.ofNullable(params.getInboxIds())),
            entry("project_ids", Optional.ofNullable(params.getProjectIds())));

        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/account_accesses", accountId),
            new RequestData(queryParams),
            AccountAccessResponse.class
        );
    }

    @Override
    public RemoveAccountAccessResponse removeAccountAccess(final long accountId, final long accountAccessId) {
        return httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/account_accesses/%d", accountId, accountAccessId),
            new RequestData(),
            RemoveAccountAccessResponse.class
        );
    }
}
