package io.mailtrap.api.contacts;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contacts.ContactSubscriptionTopic;
import io.mailtrap.model.request.contacts.CreateContact;
import io.mailtrap.model.request.contacts.CreateContactRequest;
import io.mailtrap.model.request.contacts.UpdateContact;
import io.mailtrap.model.request.contacts.UpdateContactRequest;
import io.mailtrap.model.response.contacts.ContactAction;
import io.mailtrap.model.response.contacts.ContactStatus;
import io.mailtrap.model.response.contacts.CreateContactResponse;
import io.mailtrap.model.response.contacts.GetContactResponse;
import io.mailtrap.model.response.contacts.UpdateContactResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactsImplTest extends BaseTest {
    private final String topicsEmail = "topics@example.com";
    private final String unreadableTopicsEmail = "unreadable-topics@example.com";

    private Contacts api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts",
                        "POST", "api/contacts/createContactRequest.json", "api/contacts/createContactResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts",
                        "POST", "api/contacts/createContactWithTopicsRequest.json", "api/contacts/createContactResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + URLEncoder.encode(topicsEmail, StandardCharsets.UTF_8),
                        "GET", null, "api/contacts/getContactWithTopicsResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + URLEncoder.encode(unreadableTopicsEmail, StandardCharsets.UTF_8),
                        "GET", null, "api/contacts/getContactUnreadableTopicsResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + URLEncoder.encode(topicsEmail, StandardCharsets.UTF_8),
                        "PATCH", "api/contacts/updateContactTopicsOnlyRequest.json", "api/contacts/updateContactTopicsOnlyResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + URLEncoder.encode(unreadableTopicsEmail, StandardCharsets.UTF_8),
                        "PATCH", "api/contacts/updateContactUnsubscribedFalseRequest.json", "api/contacts/updateContactTopicsOnlyResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + emailEncoded,
                        "GET", null, "api/contacts/getContactResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + contactUUIDEncoded,
                        "GET", null, "api/contacts/getContactResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + emailEncoded,
                        "DELETE", null, null),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + contactUUIDEncoded,
                        "DELETE", null, null),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + emailEncoded,
                        "PATCH", "api/contacts/updateContactByEmailRequest.json", "api/contacts/updateContactByEmailResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + contactUUIDEncoded,
                        "PATCH", "api/contacts/updateContactByIdRequest.json", "api/contacts/updateContactByIdResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).contactsApi().contacts();
    }

    @Test
    void test_createContact() {
        final CreateContactRequest request = new CreateContactRequest(new CreateContact(email, Map.of("first_name", "Nick"), List.of(1L, 2L)));

        final CreateContactResponse response = api.createContact(accountId, request);

        assertNotNull(response);
        assertEquals(contactUUID, response.getData().getId());
        assertEquals(email, response.getData().getEmail());
        assertEquals(ContactStatus.SUBSCRIBED, response.getData().getStatus());
        assertEquals(List.of(1L, 2L), response.getData().getListIds());
        assertEquals(2, response.getData().getSubscriptionTopics().size());
    }

    @Test
    void test_createContact_withSubscriptionTopics() {
        final CreateContact contact = CreateContact.builder()
                .email(email)
                .fields(Map.of("first_name", "Nick"))
                .listIds(List.of(1L, 2L))
                .subscriptionTopics(List.of(topicPreference(77L, ContactStatus.UNSUBSCRIBED)))
                .build();

        final CreateContactResponse response = api.createContact(accountId, new CreateContactRequest(contact));

        assertNotNull(response);
        assertEquals(
                List.of(topicPreference(77L, ContactStatus.UNSUBSCRIBED), topicPreference(78L, ContactStatus.SUBSCRIBED)),
                response.getData().getSubscriptionTopics());
    }

    @Test
    void test_getContact_byEmail() {
        final GetContactResponse response = api.getContact(accountId, email);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals("018dd5e3-f6d2-7c00-8f9b-e5c3f2d8a132", response.getData().getId());
        assertEquals("john.smith@example.com", response.getData().getEmail());
        assertEquals(ContactStatus.SUBSCRIBED, response.getData().getStatus());
        assertEquals(List.of(1L, 2L, 3L), response.getData().getListIds());
        assertEquals(1742820600230L, response.getData().getCreatedAt());
        assertEquals(1742820600230L, response.getData().getUpdatedAt());
        // an account without subscription topics gets no subscription_topics key
        assertNull(response.getData().getSubscriptionTopics());
    }

    @Test
    void test_getContact_withSubscriptionTopics() {
        final GetContactResponse response = api.getContact(accountId, topicsEmail);

        assertNotNull(response);
        final List<ContactSubscriptionTopic> topics = response.getData().getSubscriptionTopics();
        assertEquals(2, topics.size());
        assertEquals(77L, topics.get(0).getSubscriptionTopicId());
        assertEquals(4321L, topics.get(0).getDomainId());
        assertEquals(ContactStatus.UNSUBSCRIBED, topics.get(0).getStatus());
        assertEquals(ContactStatus.SUBSCRIBED, topics.get(1).getStatus());
    }

    @Test
    void test_getContact_withUnreadableSubscriptionTopics() {
        final GetContactResponse response = api.getContact(accountId, unreadableTopicsEmail);

        assertNotNull(response);
        assertEquals("unreadable-topics@example.com", response.getData().getEmail());
        assertNull(response.getData().getSubscriptionTopics());
    }

    @Test
    void test_getContact_byId() {
        final GetContactResponse response = api.getContact(accountId, contactUUID);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(contactUUID, response.getData().getId());
        assertEquals(ContactStatus.SUBSCRIBED, response.getData().getStatus());
    }

    @Test
    void test_deleteContact_byEmail() {
        api.deleteContact(accountId, email);
    }

    @Test
    void test_deleteContact_byId() {
        api.deleteContact(accountId, contactUUID);
    }

    @Test
    void test_updateContact_byEmail() {
        final UpdateContactRequest request = new UpdateContactRequest(new UpdateContact(email, Map.of("first_name", "Joe"), List.of(1L, 2L), List.of(3L, 4L), false));

        final UpdateContactResponse response = api.updateContact(accountId, email, request);

        assertNotNull(response);
        assertEquals(ContactAction.UPDATED, response.getAction());
        assertEquals(ContactStatus.SUBSCRIBED, response.getData().getStatus());
    }

    @Test
    void test_updateContact_byId() {
        final UpdateContactRequest request = new UpdateContactRequest(new UpdateContact(email, Map.of("zip_code", 11111), List.of(1L, 2L), List.of(3L, 4L), false));

        final UpdateContactResponse response = api.updateContact(accountId, contactUUID, request);

        assertNotNull(response);
        assertEquals(ContactAction.UPDATED, response.getAction());
        assertEquals(ContactStatus.SUBSCRIBED, response.getData().getStatus());
    }

    @Test
    void test_updateContact_subscriptionTopicsOnly() {
        final UpdateContact contact = UpdateContact.builder()
                .subscriptionTopics(List.of(topicPreference(78L, ContactStatus.UNSUBSCRIBED)))
                .build();

        // the request fixture has no unsubscribed key, so a globally unsubscribed contact stays unsubscribed
        assertNull(contact.getUnsubscribed());
        assertFalse(contact.isUnsubscribed());

        final UpdateContactResponse response = api.updateContact(accountId, topicsEmail, new UpdateContactRequest(contact));

        assertNotNull(response);
        assertEquals(ContactAction.UPDATED, response.getAction());
        assertEquals(
                List.of(topicPreference(77L, ContactStatus.UNSUBSCRIBED), topicPreference(78L, ContactStatus.UNSUBSCRIBED)),
                response.getData().getSubscriptionTopics());
    }

    @Test
    void test_updateContact_sendsUnsubscribedFalseWhenSet() {
        final UpdateContact contact = UpdateContact.builder().unsubscribed(false).build();

        final UpdateContactResponse response = api.updateContact(accountId, unreadableTopicsEmail, new UpdateContactRequest(contact));

        assertNotNull(response);
        assertFalse(contact.isUnsubscribed());
        assertTrue(UpdateContact.builder().unsubscribed(true).build().isUnsubscribed());
    }

    private ContactSubscriptionTopic topicPreference(final long subscriptionTopicId, final ContactStatus status) {
        return ContactSubscriptionTopic.builder()
                .subscriptionTopicId(subscriptionTopicId)
                .domainId(4321L)
                .status(status)
                .build();
    }
}
