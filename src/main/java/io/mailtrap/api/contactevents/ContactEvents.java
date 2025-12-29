package io.mailtrap.api.contactevents;

import io.mailtrap.model.request.contactevents.CreateContactEventRequest;
import io.mailtrap.model.response.contactevents.ContactEventResponse;

public interface ContactEvents {

    /**
     * Submit custom event for contact
     *
     * @param accountId         unique account ID
     * @param contactIdentifier Contact UUID or Email
     * @param request           body
     * @return created contact event
     */
    ContactEventResponse createContactEvent(long accountId, String contactIdentifier, CreateContactEventRequest request);
}
