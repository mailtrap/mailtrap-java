package io.mailtrap.api.contactlists;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.contactlists.ContactListResponse;

import java.util.List;

public class ContactListsImpl extends ApiResource implements ContactLists {

    public ContactListsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public List<ContactListResponse> findAll(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/contacts/lists", accountId),
            new RequestData(),
            ContactListResponse.class
        );
    }
}
