package io.mailtrap.model.request.contactexports;

import lombok.Getter;

import static io.mailtrap.model.request.contactexports.ContactExportFilterName.list_id;
import static io.mailtrap.model.request.contactexports.ContactExportFilterName.subscription_status;

@Getter
public class ContactExportFilter {

    private final String name;
    private final String operator;
    private final Object value;

    private ContactExportFilter(final String name, final String operator, final Object value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public static ContactExportFilter listIDs(final ContactExportFilterOperator operator, final Long... listIDs) {
        return new ContactExportFilter(list_id.name(), operator.getValue(), listIDs);
    }

    public static ContactExportFilter subscriptionStatus(final ContactExportFilterOperator operator, final ContactExportFilterSubscriptionStatus status) {
        return new ContactExportFilter(subscription_status.name(), operator.getValue(), status.getValue());
    }
}
