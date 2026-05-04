package io.mailtrap.api.subaccounts;

import io.mailtrap.model.request.subaccounts.CreateSubAccountRequest;
import io.mailtrap.model.response.subaccounts.SubAccount;

import java.util.List;

public interface SubAccounts {

    /**
     * Get a list of sub accounts for the specified organization.
     * Requires sub account management permissions for the organization.
     *
     * @param organizationId unique organization ID
     * @return list of sub accounts
     */
    List<SubAccount> getSubAccounts(long organizationId);

    /**
     * Create a new sub account under the specified organization.
     * Requires sub account management permissions for the organization.
     *
     * @param organizationId unique organization ID
     * @param request        sub account data
     * @return created sub account
     */
    SubAccount createSubAccount(long organizationId, CreateSubAccountRequest request);

}
