package io.mailtrap.client.api;

import io.mailtrap.api.subaccounts.SubAccounts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an API for Mailtrap Organizations functionality
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapOrganizationsApi {
    private final SubAccounts subAccounts;
}
