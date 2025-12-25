package io.mailtrap.api.contactlists;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactlists.CreateUpdateContactListRequest;
import io.mailtrap.model.response.contactlists.ContactListResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactListsImplTest extends BaseTest {
    private ContactLists api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/lists",
                "GET", null, "api/contact_lists/contactLists.json"),
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/lists",
                "POST", "api/contact_lists/createRequest.json", "api/contact_lists/contactList.json"),
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/lists/" + contactListId,
                "GET", null, "api/contact_lists/contactList.json"),
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/lists/" + contactListId,
                "PATCH", "api/contact_lists/updateRequest.json", "api/contact_lists/updatedContactList.json"),
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/lists/" + contactListId,
                "DELETE", null, null)
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).contactsApi().contactLists();
    }

    @Test
    void test_findAll() {
        final List<ContactListResponse> contacts = api.findAll(accountId);

        assertFalse(contacts.isEmpty());
        assertEquals(2, contacts.size());
    }

    @Test
    void test_create() {
        final ContactListResponse contactList = api.createContactList(accountId, new CreateUpdateContactListRequest("Customers"));

        assertNotNull(contactList);
        assertEquals(contactListId, contactList.getId());
    }

    @Test
    void test_findOne() {
        final ContactListResponse contactList = api.getContactList(accountId, contactListId);

        assertNotNull(contactList);
        assertEquals(contactListId, contactList.getId());
    }

    @Test
    void test_update() {
        final ContactListResponse contactList = api.updateContactList(accountId, contactListId, new CreateUpdateContactListRequest("Old-Customers"));

        assertNotNull(contactList);
        assertEquals(contactListId, contactList.getId());
        assertEquals("Old-Customers", contactList.getName());
    }

    @Test
    void test_delete() {
        assertDoesNotThrow(() -> api.deleteContactList(accountId, contactListId));
    }
}
