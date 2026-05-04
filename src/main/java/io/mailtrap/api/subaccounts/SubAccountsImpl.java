package io.mailtrap.api.subaccounts;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.subaccounts.CreateSubAccountRequest;
import io.mailtrap.model.response.subaccounts.SubAccount;

import java.util.List;

public class SubAccountsImpl extends ApiResource implements SubAccounts {

    public SubAccountsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public List<SubAccount> getSubAccounts(final long organizationId) {
        return httpClient.getList(
            String.format(apiHost + "/api/organizations/%d/sub_accounts", organizationId),
            new RequestData(),
            SubAccount.class
        );
    }

    @Override
    public SubAccount createSubAccount(final long organizationId, final CreateSubAccountRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/organizations/%d/sub_accounts", organizationId),
            request,
            new RequestData(),
            SubAccount.class
        );
    }
}
