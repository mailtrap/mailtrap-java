package io.mailtrap.model.request.contactexports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContactExportFilterOperator {
    EQUAL("equal");

    private final String value;
}
