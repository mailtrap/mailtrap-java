package io.mailtrap.client.api;

import io.mailtrap.api.marketingstats.MarketingStats;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an API for Mailtrap Email Marketing functionality.
 *
 * <p>Email Marketing stats use a token-scoped URL family ({@code /api/marketing}), so they are
 * grouped under their own API rather than the account-scoped general API.
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapMarketingApi {
    private final MarketingStats stats;
}
