package io.mailtrap.api.contactexports;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.contactexports.CreateContactsExportRequest;
import io.mailtrap.model.response.contactexports.ContactExportResponse;

public class ContactExportsImpl extends ApiResource implements ContactExports {

    public ContactExportsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public ContactExportResponse createContactExport(final long accountId, final CreateContactsExportRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/contacts/exports", accountId),
            request,
            new RequestData(),
            ContactExportResponse.class
        );
    }

    @Override
    public ContactExportResponse getContactExport(final long accountId, final long exportId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/contacts/exports/%d", accountId, exportId),
            new RequestData(),
            ContactExportResponse.class
        );
    }
}
