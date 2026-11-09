package io.mailtrap.api.inboxes;

import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;
import io.mailtrap.model.response.inboxes.InboxResponse;

import java.util.List;

/**
 * Interface representing the Mailtrap Sandbox API for interaction with sandboxes
 */
public interface Inboxes {

    /**
     * Create a sandbox in a project
     *
     * @param accountId unique account ID
     * @param projectId unique project ID
     * @param request   sandbox request data
     * @return the newly created sandbox with its attributes
     */
    InboxResponse createSandbox(long accountId, long projectId, CreateInboxRequest request);

    /**
     * Get sandbox attributes by sandbox ID
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the sandbox
     */
    InboxResponse getSandboxAttributes(long accountId, long sandboxId);

    /**
     * Delete a sandbox with all its emails
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the deleted sandbox
     */
    InboxResponse deleteSandbox(long accountId, long sandboxId);

    /**
     * Update sandbox name, sandbox email username
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param request   request data
     * @return the attributes of the updated sandbox
     */
    InboxResponse updateSandbox(long accountId, long sandboxId, UpdateInboxRequest request);

    /**
     * Delete all messages (emails) from sandbox
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the sandbox. <b>permissions</b> returns the permissions of the token for the sandbox.
     */
    InboxResponse cleanSandbox(long accountId, long sandboxId);

    /**
     * Mark all messages in the sandbox as read
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the sandbox. <b>permissions</b> returns the permissions of the token for the sandbox
     */
    InboxResponse markAsRead(long accountId, long sandboxId);

    /**
     * Reset SMTP credentials of the sandbox
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the sandbox. <b>permissions</b> returns the permissions of the token for the sandbox
     */
    InboxResponse resetCredentials(long accountId, long sandboxId);

    /**
     * Turn the email address of the sandbox on/off
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the sandbox. <b>permissions</b> returns the permissions of the token for the sandbox
     */
    InboxResponse enableEmailAddress(long accountId, long sandboxId);

    /**
     * Reset username of email address per sandbox
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @return the attributes of the sandbox. <b>permissions</b> returns the permissions of the token for the sandbox
     */
    InboxResponse resetEmailAddresses(long accountId, long sandboxId);

    /**
     * Get a list of sandboxes
     *
     * @param accountId unique account ID
     * @return the list of sandboxes in the account to which the API token has access. <b>permissions</b> returns the permissions of the token for the sandbox
     */
    List<InboxResponse> getSandboxes(long accountId);

    /**
     * @deprecated Use {@link #createSandbox(long, long, CreateInboxRequest)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default InboxResponse createInbox(final long accountId, final long projectId, final CreateInboxRequest request) {
        return createSandbox(accountId, projectId, request);
    }

    /**
     * @deprecated Use {@link #getSandboxAttributes(long, long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default InboxResponse getInboxAttributes(final long accountId, final long inboxId) {
        return getSandboxAttributes(accountId, inboxId);
    }

    /**
     * @deprecated Use {@link #deleteSandbox(long, long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default InboxResponse deleteInbox(final long accountId, final long inboxId) {
        return deleteSandbox(accountId, inboxId);
    }

    /**
     * @deprecated Use {@link #updateSandbox(long, long, UpdateInboxRequest)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default InboxResponse updateInbox(final long accountId, final long inboxId, final UpdateInboxRequest request) {
        return updateSandbox(accountId, inboxId, request);
    }

    /**
     * @deprecated Use {@link #cleanSandbox(long, long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default InboxResponse cleanInbox(final long accountId, final long inboxId) {
        return cleanSandbox(accountId, inboxId);
    }

    /**
     * @deprecated Use {@link #getSandboxes(long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default List<InboxResponse> getInboxes(final long accountId) {
        return getSandboxes(accountId);
    }
}
