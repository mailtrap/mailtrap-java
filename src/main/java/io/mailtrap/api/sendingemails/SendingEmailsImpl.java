package io.mailtrap.api.sendingemails;

import io.mailtrap.Constants;
import io.mailtrap.CustomValidator;
import io.mailtrap.api.apiresource.SendApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.exception.http.HttpException;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.BatchSendResponse;
import io.mailtrap.model.response.emails.SendResponse;

/**
 * Implementation of the {@link SendingEmails} interface for sending emails in the production environment.
 */
public class SendingEmailsImpl extends SendApiResource implements SendingEmails {

    public SendingEmailsImpl(final MailtrapConfig config, final CustomValidator customValidator) {
        super(config, customValidator);
        this.apiHost = Constants.EMAIL_SENDING_SEND_HOST;
    }

    @Override
    public SendResponse send(final MailtrapMail mail) {
        validateMailPayload(mail);

        final RequestData requestData = new RequestData();
        if (mail.getHeaders() != null) {
            requestData.setHeaders(mail.getHeaders());
        }

        return
            httpClient.post(
                apiHost + "/api/send",
                mail,
                requestData,
                SendResponse.class
            );
    }

    @Override
    public BatchSendResponse batchSend(final MailtrapBatchMail mail) throws HttpException, InvalidRequestBodyException {
        validateBatchPayload(mail);

        return
            httpClient.post(
                apiHost + "/api/batch",
                mail,
                new RequestData(),
                BatchSendResponse.class
            );
    }

}
