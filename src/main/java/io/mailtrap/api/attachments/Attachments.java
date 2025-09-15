package io.mailtrap.api.attachments;

import io.mailtrap.model.response.attachment.AttachmentResponse;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface representing the Mailtrap Testing API for interaction with email message attachments.
 */
public interface Attachments {

    /**
     * Get message single attachment by ID
     *
     * @param accountId    - unique account ID
     * @param inboxId      - unique inbox ID
     * @param messageId    - unique message ID
     * @param attachmentId - unique attachment ID
     * @return attachment details and download path
     */
    AttachmentResponse getSingleAttachment(long accountId, long inboxId, long messageId, long attachmentId);

    /**
     * Get message attachments by inboxId and messageId
     *
     * @param accountId      - unique account ID
     * @param inboxId        - unique inbox ID
     * @param messageId      - unique message ID
     * @param attachmentType - attachment type; optional query param
     * @return attachments with their details and download paths
     */
    List<AttachmentResponse> getAttachments(long accountId, long inboxId, long messageId, @Nullable String attachmentType);

}
