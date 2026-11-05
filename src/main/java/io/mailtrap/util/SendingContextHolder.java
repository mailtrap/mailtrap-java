package io.mailtrap.util;

import io.mailtrap.client.MailtrapClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Utility class which holds sending context (which API to use: Email Sending API, Bulk Sending API or
 * Email Sandbox API, sandbox ID for Email Sandbox API) to make it possible to perform send directly from {@link MailtrapClient}
 */
@Getter
@Builder
@Setter
public class SendingContextHolder {
    private boolean sandbox;
    private Long sandboxId;
    private boolean bulk;

    /**
     * @deprecated Use {@code getSandboxId()} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public Long getInboxId() {
        return getSandboxId();
    }

    /**
     * @deprecated Use {@code setSandboxId(Long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public void setInboxId(final Long inboxId) {
        setSandboxId(inboxId);
    }

    public static class SendingContextHolderBuilder {

        /**
         * @deprecated Use {@code sandboxId(Long)} instead. Will be removed in a future major release.
         */
        @Deprecated(since = "1.11.0")
        public SendingContextHolderBuilder inboxId(final Long inboxId) {
            return sandboxId(inboxId);
        }
    }
}
