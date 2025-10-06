package io.mailtrap.api.contactexports;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactexports.ContactExportFilter;
import io.mailtrap.model.request.contactexports.ContactExportFilterOperator;
import io.mailtrap.model.request.contactexports.ContactExportFilterSubscriptionStatus;
import io.mailtrap.model.request.contactexports.CreateContactsExportRequest;
import io.mailtrap.model.response.contactexports.ContactExportResponse;
import io.mailtrap.model.response.contactexports.ContactExportStatus;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ContactExportsImplTest extends BaseTest {

    private ContactExports api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/exports",
                "POST", "api/contactexports/createContactExportRequest.json", "api/contactexports/createContactExportResponse.json"),

            DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/contacts/exports/" + exportId,
                "GET", null, "api/contactexports/getContactExportResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).contactsApi().contactExports();
    }

    @Test
    void test_createContactExport_ids_filter() {
        final var idsFilter = ContactExportFilter.ids(ContactExportFilterOperator.EQUAL, filterExportId);
        final var subscriptionStatusFilter = ContactExportFilter.subscriptionStatus(ContactExportFilterOperator.EQUAL, ContactExportFilterSubscriptionStatus.SUBSCRIBED);

        final var request = new CreateContactsExportRequest(List.of(idsFilter, subscriptionStatusFilter));

        final ContactExportResponse contactExportResponse = api.createContactExport(accountId, request);

        assertEquals(exportId, contactExportResponse.getId());
        assertSame(ContactExportStatus.STARTED, contactExportResponse.getStatus());
    }

    @Test
    void test_getContactExport() {
        final ContactExportResponse contactExport = api.getContactExport(accountId, exportId);

        assertEquals(exportId, contactExport.getId());
        assertSame(ContactExportStatus.FINISHED, contactExport.getStatus());
    }
}
