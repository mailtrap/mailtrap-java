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

    /**
     * Delete a sub account from the specified organization.
     * Requires sub account management permissions for the organization.
     * <p>
     * Deletion is permanent and removes all sub account data. Deleting the last sub account
     * of an organization deletes the organization as well. A repeated call for the same
     * sub account fails with 404. Rate limit – 10 requests per minute per organization.
     *
     * @param organizationId unique organization ID
     * @param subAccountId   unique sub account ID
     */
    void deleteSubAccount(long organizationId, long subAccountId);

}
