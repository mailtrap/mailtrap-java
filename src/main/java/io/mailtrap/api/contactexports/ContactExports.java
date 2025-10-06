package io.mailtrap.api.contactexports;

import io.mailtrap.model.request.contactexports.CreateContactsExportRequest;
import io.mailtrap.model.response.contactexports.ContactExportResponse;

public interface ContactExports {

    /**
     * Create a new Contact Export
     *
     * @param accountId unique account ID
     * @param request   payload
     * @return created contact export
     */
    ContactExportResponse createContactExport(long accountId, CreateContactsExportRequest request);

    /**
     * Get Contact Export
     *
     * @param accountId unique account ID
     * @param exportId  unique contact export ID
     * @return contact export
     */
    ContactExportResponse getContactExport(long accountId, long exportId);
}
