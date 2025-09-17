package io.mailtrap.api.contactimports;

import io.mailtrap.Constants;
import io.mailtrap.MailtrapValidator;
import io.mailtrap.api.apiresource.ApiResourceWithValidation;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.contactimports.ImportContactsRequest;
import io.mailtrap.model.response.contactimports.ContactsImportResponse;
import io.mailtrap.model.response.contactimports.CreateContactsImportResponse;

public class ContactImportsImpl extends ApiResourceWithValidation implements ContactImports {

    public ContactImportsImpl(final MailtrapConfig config, final MailtrapValidator validator) {
        super(config, validator);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public CreateContactsImportResponse importContacts(final long accountId, final ImportContactsRequest request) {

        validateRequestBodyAndThrowException(request);

        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/contacts/imports", accountId),
            request,
            new RequestData(),
            CreateContactsImportResponse.class
        );
    }

    @Override
    public ContactsImportResponse getContactImport(final long accountId, final long importId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/contacts/imports/%d", accountId, importId),
            new RequestData(),
            ContactsImportResponse.class
        );
    }
}
