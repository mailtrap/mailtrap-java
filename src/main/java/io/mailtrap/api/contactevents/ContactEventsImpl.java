package io.mailtrap.api.contactevents;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.contactevents.CreateContactEventRequest;
import io.mailtrap.model.response.contactevents.ContactEventResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ContactEventsImpl extends ApiResource implements ContactEvents {

    public ContactEventsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public ContactEventResponse createContactEvent(final long accountId, final String contactIdentifier, final CreateContactEventRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/contacts/%s/events", accountId, URLEncoder.encode(contactIdentifier, StandardCharsets.UTF_8)),
            request,
            new RequestData(),
            ContactEventResponse.class
        );
    }
}
