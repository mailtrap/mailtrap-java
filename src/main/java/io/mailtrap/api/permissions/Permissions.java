package io.mailtrap.api.permissions;

import io.mailtrap.model.request.permissions.ManagePermissionsRequest;
import io.mailtrap.model.response.permissions.ManagePermissionsResponse;
import io.mailtrap.model.response.permissions.Resource;

import java.util.List;

/**
 * Interface representing the Mailtrap General API for interaction with permissions
 */
public interface Permissions {

    /**
     * Manage user or token permissions.
     * If provided combination of resource_type and resource_id that already exists, the permission is updated.
     * If the combination doesn't exist, the permission is created.
     *
     * @param accountAccessId unique account access ID
     * @param accountId       unique account ID
     * @param request         request body
     * @return message about successful update of permissions
     */
    ManagePermissionsResponse managePermissions(long accountAccessId, long accountId, ManagePermissionsRequest request);

    /**
     * Get all resources in account (Inboxes, Projects, Domains, Billing and Account itself) to which the token has admin access
     *
     * @param accountId unique account ID
     * @return the resources nested according to their hierarchy.
     */
    List<Resource> getResources(long accountId);

}
