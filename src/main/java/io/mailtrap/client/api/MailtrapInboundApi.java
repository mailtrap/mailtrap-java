package io.mailtrap.client.api;

import io.mailtrap.api.inbound.InboundFolders;
import io.mailtrap.api.inbound.InboundForwardRules;
import io.mailtrap.api.inbound.InboundInboxes;
import io.mailtrap.api.inbound.InboundMessages;
import io.mailtrap.api.inbound.InboundThreads;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Groups the token-scoped Inbound Email API resources (folders, inboxes,
 * messages, threads, forward rules).
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapInboundApi {

    private final InboundFolders folders;

    private final InboundInboxes inboxes;

    private final InboundMessages messages;

    private final InboundThreads threads;

    private final InboundForwardRules forwardRules;
}
