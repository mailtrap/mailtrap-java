package io.mailtrap.config;

import io.mailtrap.exception.BaseMailtrapException;
import io.mailtrap.http.CustomHttpClient;
import lombok.Getter;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration class for Mailtrap SDK.
 */
@Getter
public class MailtrapConfig {

    /**
     * The duration for connection timeout.
     */
    private final Duration connectionTimeout;

    /**
     * The authentication token used for accessing the Mailtrap API.
     */
    private final String token;

    /**
     * The custom HTTP client.
     */
    private final CustomHttpClient httpClient;

    /**
     * Indicates whether to use Email Testing API
     */
    private final boolean sandbox;

    /**
     * Indicates whether to use Bulk Sending API
     */
    private final boolean bulk;

    /**
     * Inbox ID. Should be used alongside with {@link #sandbox}, as Email Testing API requires inbox ID
     */
    private final Long inboxId;

    private MailtrapConfig(final Builder builder) {
        if (builder.sandbox && builder.bulk) {
            throw new BaseMailtrapException("Bulk mode is not applicable for Testing API");
        }
        if (builder.sandbox && Objects.isNull(builder.inboxId)) {
            throw new BaseMailtrapException("Testing API requires inbox ID");
        }

        this.connectionTimeout = builder.connectionTimeout;
        this.token = builder.token;
        this.httpClient = builder.httpClient;
        this.sandbox = builder.sandbox;
        this.bulk = builder.bulk;
        this.inboxId = builder.inboxId;
    }

    public static class Builder {
        private Duration connectionTimeout;
        private String token;
        private CustomHttpClient httpClient;
        private boolean sandbox;
        private boolean bulk;
        private Long inboxId;

        public Builder connectionTimeout(final Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder token(final String token) {
            this.token = token;
            return this;
        }

        public Builder httpClient(final CustomHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder sandbox(final boolean sandbox) {
            this.sandbox = sandbox;
            return this;
        }

        public Builder bulk(final boolean bulk) {
            this.bulk = bulk;
            return this;
        }

        public Builder inboxId(final Long inboxId) {
            this.inboxId = inboxId;
            return this;
        }

        public MailtrapConfig build() {
            return new MailtrapConfig(this);
        }
    }
}
