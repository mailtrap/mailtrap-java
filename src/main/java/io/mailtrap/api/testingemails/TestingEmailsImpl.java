package io.mailtrap.api.testingemails;

import io.mailtrap.Constants;
import io.mailtrap.MailtrapValidator;
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
 * Implementation of the {@link TestingEmails} interface for sending emails in the sandbox environment.
 */
public class TestingEmailsImpl extends SendApiResource implements TestingEmails {

    public TestingEmailsImpl(final MailtrapConfig config, final MailtrapValidator mailtrapValidator) {
        super(config, mailtrapValidator);
        this.apiHost = Constants.EMAIL_SANDBOX_SEND_HOST;
    }

    @Override
    public SendResponse send(final MailtrapMail mail, final long sandboxId) {
        validateMailPayload(mail);
        RequestData requestData = new RequestData();
        if (mail.getHeaders() != null) {
            requestData.setHeaders(mail.getHeaders());
        }

        return
            httpClient.post(
                String.format(apiHost + "/api/send/%d", sandboxId),
                mail,
                requestData,
                SendResponse.class
            );
    }

    @Override
    public BatchSendResponse batchSend(final MailtrapBatchMail mail, final long sandboxId) throws HttpException, InvalidRequestBodyException {
        validateBatchPayload(mail);

        return
            httpClient.post(
                String.format(apiHost + "/api/batch/%d", sandboxId),
                mail, new RequestData(),
                BatchSendResponse.class
            );

    }
}
