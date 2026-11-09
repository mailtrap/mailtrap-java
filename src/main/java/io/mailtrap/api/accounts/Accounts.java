package io.mailtrap.api.accounts;

import io.mailtrap.model.response.accounts.AccountsResponse;

import java.util.List;

/**
 * Interface representing the Mailtrap General API for interaction with accounts
 */
public interface Accounts {

    /**
     * Get a list of your Mailtrap accounts.
     *
     * @return the list of accounts to which the API token has access
     */
    List<AccountsResponse> getAllAccounts();

}
