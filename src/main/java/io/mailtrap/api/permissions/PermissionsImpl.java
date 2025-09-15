package io.mailtrap.api.permissions;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.permissions.ManagePermissionsRequest;
import io.mailtrap.model.response.permissions.ManagePermissionsResponse;
import io.mailtrap.model.response.permissions.Resource;

import java.util.List;

public class PermissionsImpl extends ApiResource implements Permissions {

    public PermissionsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public ManagePermissionsResponse managePermissions(final long accountAccessId, final long accountId, final ManagePermissionsRequest request) {
        return httpClient.put(
            String.format(apiHost + "/api/accounts/%d/account_accesses/%d/permissions/bulk", accountId, accountAccessId),
            request,
            new RequestData(),
            ManagePermissionsResponse.class
        );
    }

    @Override
    public List<Resource> getResources(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/permissions/resources", accountId),
            new RequestData(),
            Resource.class
        );
    }

}
