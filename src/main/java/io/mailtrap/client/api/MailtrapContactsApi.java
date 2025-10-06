package io.mailtrap.client.api;

import io.mailtrap.api.contactexports.ContactExports;
import io.mailtrap.api.contactfields.ContactFields;
import io.mailtrap.api.contactimports.ContactImports;
import io.mailtrap.api.contactlists.ContactLists;
import io.mailtrap.api.contacts.Contacts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an API for Mailtrap Contacts functionality
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapContactsApi {
    private final ContactLists contactLists;
    private final Contacts contacts;
    private final ContactImports contactImports;
    private final ContactFields contactFields;
    private final ContactExports contactExports;
}
