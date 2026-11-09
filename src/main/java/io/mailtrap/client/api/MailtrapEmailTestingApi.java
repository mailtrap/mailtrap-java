package io.mailtrap.client.api;

import io.mailtrap.api.attachments.Attachments;
import io.mailtrap.api.messages.Messages;
import io.mailtrap.api.inboxes.Inboxes;
import io.mailtrap.api.projects.Projects;
import io.mailtrap.api.testingemails.TestingEmails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an API for Mailtrap Email Sandbox functionality
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapEmailTestingApi {
    private final TestingEmails emails;
    private final Attachments attachments;
    private final Inboxes sandboxes;
    private final Projects projects;
    private final Messages messages;

    /**
     * @return API for managing sandboxes
     */
    public Inboxes sandboxes() {
        return sandboxes;
    }

    /**
     * @deprecated Use {@link #sandboxes()} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public Inboxes inboxes() {
        return sandboxes();
    }
}
