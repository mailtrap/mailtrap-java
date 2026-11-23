package io.mailtrap.model.response.contacts;

import lombok.Data;

@Data
public class CreateContactResponse {

    /**
     * The created contact.
     */
    private GetContactData data;

    /**
     * @deprecated always {@code null}: the API returns the contact under {@code data}. Use
     * {@code getData().getId()}.
     */
    @Deprecated
    private String id;

    /**
     * @deprecated always {@code null}: the API returns the contact under {@code data}. Use
     * {@code getData().getStatus()}.
     */
    @Deprecated
    private ContactStatus status;

}
