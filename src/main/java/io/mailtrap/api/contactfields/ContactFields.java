package io.mailtrap.api.contactfields;

import io.mailtrap.model.request.contactfields.CreateContactFieldRequest;
import io.mailtrap.model.request.contactfields.UpdateContactFieldRequest;
import io.mailtrap.model.response.contactfields.ContactFieldResponse;

import java.util.List;

public interface ContactFields {

    /**
     * Get all Contact Fields existing in your account
     *
     * @param accountId unique account ID
     * @return existing contact fields
     */
    List<ContactFieldResponse> getAllContactFields(long accountId);

    /**
     * Create new Contact Fields (up to 40)
     *
     * @param accountId unique account ID
     * @param request   contact field data
     * @return attributes of the created contact field
     */
    ContactFieldResponse createContactField(long accountId, CreateContactFieldRequest request);

    /**
     * Get Contact Field by id
     *
     * @param accountId unique account ID
     * @param fieldId   Unique Contact Field ID
     * @return attributes of the contact field
     */
    ContactFieldResponse getContactField(long accountId, long fieldId);

    /**
     * Update existing Contact Field
     *
     * @param accountId unique account ID
     * @param fieldId   Unique Contact Field ID
     * @param request   update data. You cannot change data_type of the field
     * @return attributes of the contact field
     */
    ContactFieldResponse updateContactField(long accountId, long fieldId, UpdateContactFieldRequest request);

    /**
     * Delete existing Contact Field.
     * You cannot delete a Contact Field which is used in Automations, Email Campaigns, and in conditions of Contact Segments
     *
     * @param accountId unique account ID
     * @param fieldId   Unique Contact Field ID
     */
    void deleteContactField(long accountId, long fieldId);

}
