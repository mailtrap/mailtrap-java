package io.mailtrap.model.request.contactexports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ContactExportFilterSubscriptionStatus {
    SUBSCRIBED("subscribed"),
    UNSUBSCRIBED("unsubscribed");

    private final String value;
}
