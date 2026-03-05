package io.mailtrap.client.api;

import io.mailtrap.api.accountaccesses.AccountAccesses;
import io.mailtrap.api.accounts.Accounts;
import io.mailtrap.api.billing.Billing;
import io.mailtrap.api.permissions.Permissions;
import io.mailtrap.api.stats.Stats;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an API for Mailtrap General functionality
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapGeneralApi {
    private final AccountAccesses accountAccesses;
    private final Accounts accounts;
    private final Billing billing;
    private final Permissions permissions;
    private final Stats stats;
}
