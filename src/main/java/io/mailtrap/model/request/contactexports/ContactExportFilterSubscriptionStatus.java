package io.mailtrap.model.request.contactexports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContactExportFilterSubscriptionStatus {
    SUBSCRIBED("subscribed"),
    UNSUBSCRIBED("unsubscribed");

    private final String value;
}
