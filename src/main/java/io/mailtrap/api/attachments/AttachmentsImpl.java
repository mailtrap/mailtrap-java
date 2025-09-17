package io.mailtrap.api.attachments;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.attachment.AttachmentResponse;

import java.util.List;
import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class AttachmentsImpl extends ApiResource implements Attachments {

    public AttachmentsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public AttachmentResponse getSingleAttachment(final long accountId, final long inboxId, final long messageId, final long attachmentId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/attachments/%d", accountId, inboxId, messageId, attachmentId),
            new RequestData(),
            AttachmentResponse.class);
    }

    @Override
    public List<AttachmentResponse> getAttachments(final long accountId, final long inboxId, final long messageId, final String attachmentType) {
        final var queryParams = RequestData.buildQueryParams(
            entry("attachment_type", Optional.ofNullable(attachmentType)));

        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/attachments", accountId, inboxId, messageId),
            new RequestData(queryParams),
            AttachmentResponse.class);
    }
}
