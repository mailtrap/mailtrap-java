package io.mailtrap.api.bulkemails;

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
 * Implementation of the {@link BulkEmails} interface for bulk sending emails.
 */
public class BulkEmailsImpl extends SendApiResource implements BulkEmails {

    public BulkEmailsImpl(MailtrapConfig config, CustomValidator customValidator) {
        super(config, customValidator);
        this.apiHost = Constants.BULK_SENDING_HOST;
    }

    @Override
    public SendResponse send(MailtrapMail mail) {
        validateMailPayload(mail);
        RequestData requestData = new RequestData();
        if (mail.getHeaders() != null) {
            requestData.setHeaders(mail.getHeaders());
        }
        return httpClient.post(apiHost + "/api/send", mail, requestData, SendResponse.class);
    }

    @Override
    public BatchSendResponse batchSend(MailtrapBatchMail mail) throws HttpException, InvalidRequestBodyException {
        validateBatchPayload(mail);

        return
            httpClient.post(apiHost + "/api/batch", mail, new RequestData(), BatchSendResponse.class);

    }
}
