package io.mailtrap.api.accounts;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.accounts.AccountsResponse;

import java.util.List;

public class AccountsImpl extends ApiResource implements Accounts {

    public AccountsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public List<AccountsResponse> getAllAccounts() {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts"),
            new RequestData(),
            AccountsResponse.class
        );
    }
}
