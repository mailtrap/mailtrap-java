package io.mailtrap.api.contactevents;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactevents.CreateContactEventRequest;
import io.mailtrap.model.response.contactevents.ContactEventResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContactEventsImplTest extends BaseTest {
    private ContactEvents api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/" + contactEventIdEncoded + "/events",
                "POST", "api/contactevents/createRequest.json", "api/contactevents/contactEvent.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).contactsApi().contactEvents();
    }

    @Test
    void test_createContactEvent() {
        final CreateContactEventRequest request = new CreateContactEventRequest("UserLogin", Map.of(
            "user_id", 11,
            "user_name", "Dillan Doe",
            "is_active", true
        ));

        final ContactEventResponse created = api.createContactEvent(accountId, contactEventId, request);

        assertNotNull(created);
        assertEquals(contactEventId, created.getContactId());
        assertEquals(3, created.getParams().size());
        assertEquals("Dillan Doe", created.getParams().get("user_name"));
    }
}
