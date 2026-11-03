package io.mailtrap.api.attachments;

import io.mailtrap.model.response.attachment.AttachmentResponse;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface representing the Mailtrap Sandbox API for interaction with email message attachments.
 */
public interface Attachments {

    /**
     * Get message single attachment by ID
     *
     * @param accountId    - unique account ID
     * @param sandboxId    - unique sandbox ID
     * @param messageId    - unique message ID
     * @param attachmentId - unique attachment ID
     * @return attachment details and download path
     */
    AttachmentResponse getSingleAttachment(long accountId, long sandboxId, long messageId, long attachmentId);

    /**
     * Get message attachments by sandboxId and messageId
     *
     * @param accountId      - unique account ID
     * @param sandboxId      - unique sandbox ID
     * @param messageId      - unique message ID
     * @param attachmentType - attachment type; optional query param
     * @return attachments with their details and download paths
     */
    List<AttachmentResponse> getAttachments(long accountId, long sandboxId, long messageId, @Nullable String attachmentType);

}
