package io.mailtrap.api.contactlists;

import io.mailtrap.model.request.contactlists.CreateUpdateContactListRequest;
import io.mailtrap.model.response.contactlists.ContactListResponse;

import java.util.List;

public interface ContactLists {

    /**
     * Get all contact lists existing in account
     *
     * @param accountId unique account ID
     * @return contacts list
     */
    List<ContactListResponse> findAll(long accountId);

    /**
     * Create new Contact List
     *
     * @param accountId unique account ID
     * @param request   body
     * @return created contact list
     */
    ContactListResponse createContactList(long accountId, CreateUpdateContactListRequest request);

    /**
     * Get a contact list by ID
     *
     * @param accountId     unique account ID
     * @param contactListId unique contact list ID
     * @return found contact list
     */
    ContactListResponse getContactList(long accountId, long contactListId);

    /**
     * Update existing Contact List
     *
     * @param accountId     unique account ID
     * @param contactListId unique contact list ID
     * @param request       body
     * @return updated contact list
     */
    ContactListResponse updateContactList(long accountId, long contactListId, CreateUpdateContactListRequest request);

    /**
     * Delete existing Contact List
     *
     * @param accountId     unique account ID
     * @param contactListId unique contact list ID
     */
    void deleteContactList(long accountId, long contactListId);
}
