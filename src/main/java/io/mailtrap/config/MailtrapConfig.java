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
     * Indicates whether to use Email Sandbox API
     */
    private final boolean sandbox;

    /**
     * Indicates whether to use Bulk Sending API
     */
    private final boolean bulk;

    /**
     * Sandbox ID. Should be used alongside with {@link #sandbox}, as Email Sandbox API requires sandbox ID
     */
    private final Long sandboxId;

    private MailtrapConfig(final Builder builder) {
        if (builder.sandbox && builder.bulk) {
            throw new BaseMailtrapException("Bulk mode is not applicable for Sandbox API");
        }
        if (builder.sandbox && Objects.isNull(builder.sandboxId)) {
            throw new BaseMailtrapException("Sandbox API requires sandbox ID");
        }

        this.connectionTimeout = builder.connectionTimeout;
        this.token = builder.token;
        this.httpClient = builder.httpClient;
        this.sandbox = builder.sandbox;
        this.bulk = builder.bulk;
        this.sandboxId = builder.sandboxId;
    }

    /**
     * @return the sandbox ID the Email Sandbox API sends to
     */
    public Long getSandboxId() {
        return sandboxId;
    }

    /**
     * @deprecated Use {@link #getSandboxId()} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public Long getInboxId() {
        return getSandboxId();
    }

    public static class Builder {
        private Duration connectionTimeout;
        private String token;
        private CustomHttpClient httpClient;
        private boolean sandbox;
        private boolean bulk;
        private Long sandboxId;

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

        public Builder sandboxId(final Long sandboxId) {
            this.sandboxId = sandboxId;
            return this;
        }

        /**
         * @deprecated Use {@link #sandboxId(Long)} instead. Will be removed in a future major release.
         */
        @Deprecated(since = "1.11.0")
        public Builder inboxId(final Long inboxId) {
            return sandboxId(inboxId);
        }

        public MailtrapConfig build() {
            return new MailtrapConfig(this);
        }
    }
}
