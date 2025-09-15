package io.mailtrap.api.testingemails;

import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.exception.http.HttpException;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.BatchSendResponse;
import io.mailtrap.model.response.emails.SendResponse;

/**
 * Interface representing the Mailtrap Testing API for sending emails.
 */
public interface TestingEmails {

    /**
     * Sends an email
     *
     * @param mail    The email message to be sent.
     * @param inboxId ID of the testing inbox, where to send an email.
     * @return A response indicating the result of the send operation.
     * <p>
     * @throws HttpException               If there is an HTTP-related error during the send operation.
     * @throws InvalidRequestBodyException If the request body is invalid.
     */
    SendResponse send(MailtrapMail mail, long inboxId) throws HttpException, InvalidRequestBodyException;

    BatchSendResponse batchSend(MailtrapBatchMail mail, long inboxId) throws HttpException, InvalidRequestBodyException;
}
