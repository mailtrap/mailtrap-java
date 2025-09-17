package io.mailtrap.api.contactfields;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.ContactFieldDataType;
import io.mailtrap.model.request.contactfields.CreateContactFieldRequest;
import io.mailtrap.model.request.contactfields.UpdateContactFieldRequest;
import io.mailtrap.model.response.contactfields.ContactFieldResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactFieldsImplTest extends BaseTest {

  private ContactFields api;

  @BeforeEach
  public void init() {
    final TestHttpClient httpClient = new TestHttpClient(List.of(
        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/fields",
            "GET", null, "api/contactfields/getAllContactFieldsResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/fields",
            "POST", "api/contactfields/createContactFieldRequest.json", "api/contactfields/createContactFieldResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/fields/" + getFieldId,
            "GET", null, "api/contactfields/getContactFieldResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/fields/" + updateFieldId,
            "PATCH", "api/contactfields/updateContactFieldRequest.json", "api/contactfields/updateContactFieldResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/fields/" + deleteFieldId,
            "DELETE", null, null)
    ));

    final MailtrapConfig testConfig = new MailtrapConfig.Builder()
        .httpClient(httpClient)
        .token("dummy_token")
        .build();

    api = MailtrapClientFactory.createMailtrapClient(testConfig).contactsApi().contactFields();
  }

  @Test
  void test_getAllContactFields() {
    final List<ContactFieldResponse> allContactFields = api.getAllContactFields(accountId);

    assertEquals(3, allContactFields.size());
    assertEquals("First name", allContactFields.get(0).getName());
    assertEquals(ContactFieldDataType.TEXT, allContactFields.get(0).getDataType());
  }

  @Test
  void test_createContactField() {
    final ContactFieldResponse created = api.createContactField(accountId, new CreateContactFieldRequest("My Contact Field", ContactFieldDataType.BOOLEAN, "my_contact_field"));

    assertNotNull(created);
    assertEquals(ContactFieldDataType.BOOLEAN, created.getDataType());
    assertEquals(1337, created.getId());
  }

  @Test
  void test_getContactField() {
    final ContactFieldResponse contactField = api.getContactField(accountId, getFieldId);

    assertNotNull(contactField);
    assertEquals("First name", contactField.getName());
    assertEquals(ContactFieldDataType.TEXT, contactField.getDataType());
    assertEquals(777, contactField.getId());
  }

  @Test
  void test_updateContactField() {
    final ContactFieldResponse contactFieldResponse = api.updateContactField(accountId, updateFieldId, new UpdateContactFieldRequest("My Updated Contact Field", "my_updated_contact_field"));

    assertNotNull(contactFieldResponse);
    assertEquals(ContactFieldDataType.TEXT, contactFieldResponse.getDataType());
    assertEquals(updateFieldId, contactFieldResponse.getId());
  }

  @Test
  void test_deleteContactField() {
    assertDoesNotThrow(() -> api.deleteContactField(accountId, deleteFieldId));
  }

}
