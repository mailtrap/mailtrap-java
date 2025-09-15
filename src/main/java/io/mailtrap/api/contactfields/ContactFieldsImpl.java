package io.mailtrap.api.contactfields;

import io.mailtrap.CustomValidator;
import io.mailtrap.api.apiresource.ApiResourceWithValidation;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.contactfields.CreateContactFieldRequest;
import io.mailtrap.model.request.contactfields.UpdateContactFieldRequest;
import io.mailtrap.model.response.contactfields.ContactFieldResponse;

import java.util.List;

import static io.mailtrap.Constants.GENERAL_HOST;

public class ContactFieldsImpl extends ApiResourceWithValidation implements ContactFields {

    public ContactFieldsImpl(final MailtrapConfig config, final CustomValidator customValidator) {
        super(config, customValidator);
        this.apiHost = GENERAL_HOST;
    }

    @Override
    public List<ContactFieldResponse> getAllContactFields(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/contacts/fields", accountId),
            new RequestData(),
            ContactFieldResponse.class
        );
    }

    @Override
    public ContactFieldResponse createContactField(final long accountId, CreateContactFieldRequest request) {

        validateRequestBodyAndThrowException(request);

        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/contacts/fields", accountId),
            request,
            new RequestData(),
            ContactFieldResponse.class
        );
    }

    @Override
    public ContactFieldResponse getContactField(final long accountId, final long fieldId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/contacts/fields/%d", accountId, fieldId),
            new RequestData(),
            ContactFieldResponse.class
        );
    }

    @Override
    public ContactFieldResponse updateContactField(final long accountId, final long fieldId, UpdateContactFieldRequest request) {

        validateRequestBodyAndThrowException(request);

        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/contacts/fields/%d", accountId, fieldId),
            request,
            new RequestData(),
            ContactFieldResponse.class
        );
    }

    @Override
    public void deleteContactField(final long accountId, final long fieldId) {
        httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/contacts/fields/%d", accountId, fieldId),
            new RequestData(),
            Void.class
        );
    }
}
