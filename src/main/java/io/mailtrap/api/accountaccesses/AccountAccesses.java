package io.mailtrap.api.accountaccesses;

import io.mailtrap.model.request.accountaccesses.ListAccountAccessQueryParams;
import io.mailtrap.model.response.accountaccesses.AccountAccessResponse;
import io.mailtrap.model.response.accountaccesses.RemoveAccountAccessResponse;

import java.util.List;

/**
 * Interface representing the Mailtrap Testing API for interaction with account accesses
 */
public interface AccountAccesses {

    /**
     * Get list of account accesses for which specifier_type is User or Invite.
     * Account admin/owner permissions for this endpoint to work.
     *
     * @param accountId unique account ID
     * @param params    additional query params. If specified - the endpoint will return account accesses for these resources
     * @return account access details
     */
    List<AccountAccessResponse> listUserAndInviteAccountAccesses(long accountId, ListAccountAccessQueryParams params);

    /**
     * If specifier type is User, it removes user permissions.
     * If specifier type is Invite or ApiToken, it removes specifier along with permissions.
     * <p>
     * Account admin/owner permissions for this endpoint to work
     *
     * @param accountId       unique account ID
     * @param accountAccessId unique account access ID
     * @return confirmation of successful deletion and id of the deleted access
     */
    RemoveAccountAccessResponse removeAccountAccess(long accountId, long accountAccessId);

}
