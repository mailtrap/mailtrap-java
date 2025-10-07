package io.mailtrap.examples.contactexports;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contactexports.ContactExportFilter;
import io.mailtrap.model.request.contactexports.ContactExportFilterOperator;
import io.mailtrap.model.request.contactexports.CreateContactsExportRequest;

import java.util.List;

public class ContactExportsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;
    private static final long CONTACT_LIST_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var exportFilter = ContactExportFilter.ids(ContactExportFilterOperator.EQUAL, CONTACT_LIST_ID);

        final var createExportRequest = new CreateContactsExportRequest(List.of(exportFilter));

        final var createExportResponse = client.contactsApi().contactExports()
            .createContactExport(ACCOUNT_ID, createExportRequest);

        System.out.println(createExportResponse);

        var contactExportResponse = client.contactsApi().contactExports()
            .getContactExport(ACCOUNT_ID, createExportResponse.getId());

        System.out.println(contactExportResponse);
    }

}
