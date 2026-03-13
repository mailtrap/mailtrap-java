package io.mailtrap.client.api;

import io.mailtrap.api.sendingdomains.SendingDomains;
import io.mailtrap.api.sendingemails.SendingEmails;
import io.mailtrap.api.stats.Stats;
import io.mailtrap.api.suppressions.Suppressions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an API for Mailtrap Sending functionality
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapEmailSendingApi {
    private final SendingEmails emails;
    private final SendingDomains domains;
    private final Suppressions suppressions;
    private final Stats stats;
}
