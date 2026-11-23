package io.mailtrap.api.contacts;

import io.mailtrap.model.request.contacts.CreateContactRequest;
import io.mailtrap.model.request.contacts.UpdateContactRequest;
import io.mailtrap.model.response.contacts.CreateContactResponse;
import io.mailtrap.model.response.contacts.GetContactResponse;
import io.mailtrap.model.response.contacts.UpdateContactResponse;

public interface Contacts {

    /**
     * Create a new contact
     *
     * @param accountId unique account ID
     * @param request   required contact data
     * @return created contact
     * @throws io.mailtrap.exception.http.HttpServerException of status {@code 503} when the contact was saved but its
     *         subscription topic preferences were not: send them again in an update of the contact its message names
     */
    CreateContactResponse createContact(long accountId, CreateContactRequest request);

    /**
     * Get contact using id or email (URL encoded)
     *
     * @param accountId unique account ID
     * @param idOrEmail contact ID or Email
     * @return contact details
     */
    GetContactResponse getContact(long accountId, String idOrEmail);

    /**
     * Delete contact using id or email (URL encoded)
     *
     * @param accountId unique account ID
     * @param idOrEmail contact ID or Email
     */
    void deleteContact(long accountId, String idOrEmail);

    /**
     * Update contact using id or email (URL encoded)
     *
     * @param accountId unique account ID
     * @param idOrEmail contact ID or Email
     * @param request   request body
     * @return updated contact
     * @throws io.mailtrap.exception.http.HttpServerException of status {@code 503} when the contact was saved but its
     *         subscription topic preferences were not: send them again in an update of the contact its message names
     */
    UpdateContactResponse updateContact(long accountId, String idOrEmail,
                                        UpdateContactRequest request);

}
