package io.mailtrap.api.contacts;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.contacts.CreateContactRequest;
import io.mailtrap.model.request.contacts.UpdateContactRequest;
import io.mailtrap.model.response.contacts.CreateContactResponse;
import io.mailtrap.model.response.contacts.UpdateContactResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ContactsImpl extends ApiResource implements Contacts {

    public ContactsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public CreateContactResponse createContact(final long accountId, final CreateContactRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%s/contacts", accountId),
            request,
            new RequestData(),
            CreateContactResponse.class
        );
    }

    @Override
    public void deleteContact(final long accountId, final String idOrEmail) {
        httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/contacts/%s", accountId,
                URLEncoder.encode(idOrEmail, StandardCharsets.UTF_8)),
            new RequestData(),
            Void.class
        );
    }

    @Override
    public UpdateContactResponse updateContact(final long accountId, final String idOrEmail, final UpdateContactRequest request) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/contacts/%s", accountId,
                URLEncoder.encode(idOrEmail, StandardCharsets.UTF_8)),
            request,
            new RequestData(),
            UpdateContactResponse.class
        );
    }
}
