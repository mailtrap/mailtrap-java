package io.mailtrap.api.contactimports;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactimports.Contact;
import io.mailtrap.model.request.contactimports.ImportContactsRequest;
import io.mailtrap.model.response.contactimports.ContactImportStatus;
import io.mailtrap.model.response.contactimports.ContactsImportResponse;
import io.mailtrap.model.response.contactimports.CreateContactsImportResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ContactImportsImplTest extends BaseTest {

    private ContactImports api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/imports",
                "POST", "api/contactimports/createContactsImportRequest.json", "api/contactimports/createContactsImportResponse.json"),

            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/imports/" + importId,
                "GET", null, "api/contactimports/getContactsImportResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).contactsApi().contactImports();
    }

    @Test
    void test_importContacts() {
        final var firstContact = new Contact("customer1@example.com", Map.of("full_name", "Jane Doe"), List.of(1L), List.of(2L));
        final var secondContact = new Contact("customer2@example.com", Map.of("full_name", "John Doe"), List.of(3L), List.of(4L));
        final var request = new ImportContactsRequest(List.of(firstContact, secondContact));

        final CreateContactsImportResponse contactImportResponse = api.importContacts(accountId, request);

        assertEquals(importId, contactImportResponse.getId());
        assertSame(ContactImportStatus.STARTED, contactImportResponse.getStatus());
    }

    @Test
    void test_importContacts_should_fail_validation() {
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> api.importContacts(accountId, new ImportContactsRequest(generateContacts())));

        assertTrue(exception.getMessage().contains("contacts=Maximum 50000 contacts per request"));
    }

    private List<Contact> generateContacts() {
        final List<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < 50001; i++) {
            contacts.add(new Contact(String.format("stub_contact_%d@example.com", i), Map.of(), List.of(), List.of()));
        }

        return contacts;
    }

    @Test
    void test_getContactImport() {
        final ContactsImportResponse contactImport = api.getContactImport(accountId, importId);

        assertEquals(importId, contactImport.getId());
        assertSame(ContactImportStatus.FINISHED, contactImport.getStatus());
        assertEquals(1L, contactImport.getCreatedContactsCount());
        assertEquals(3L, contactImport.getUpdatedContactsCount());
        assertEquals(3L, contactImport.getContactsOverLimitCount());
    }
}
