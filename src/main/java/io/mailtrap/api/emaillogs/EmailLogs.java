package io.mailtrap.api.emaillogs;

import io.mailtrap.model.request.emaillogs.EmailLogsListFilters;
import io.mailtrap.model.response.emaillogs.EmailLogsListResponse;
import io.mailtrap.model.response.emaillogs.EmailLogMessage;

/**
 * API for listing and retrieving email sending logs.
 */
public interface EmailLogs {

    /**
     * Returns a paginated list of email logs for the account.
     *
     * @param accountId   account ID
     * @param searchAfter optional cursor (message_id UUID from previous response
     *                    next_page_cursor) for the next page
     * @param filters     optional filters; pass null or empty to omit
     * @return paginated list with messages, total_count, and next_page_cursor
     */
    EmailLogsListResponse list(long accountId, String searchAfter, EmailLogsListFilters filters);

    /**
     * Returns a single email log message by its UUID.
     *
     * @param accountId        account ID
     * @param sendingMessageId message UUID
     * @return the message with details and events, or throws if not found
     */
    EmailLogMessage get(long accountId, String sendingMessageId);
}
