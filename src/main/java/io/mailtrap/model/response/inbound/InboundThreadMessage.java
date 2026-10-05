package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * A message inside a thread. Only {@code visibilityStatus} and {@code direction}
 * are guaranteed; {@code placeholder} entries omit the rest.
 */
@Data
public class InboundThreadMessage {

    @JsonProperty("visibility_status")
    private InboundMessageVisibilityStatus visibilityStatus;

    private InboundMessageDirection direction;

    private String id;

    @JsonProperty("message_group_id")
    private String messageGroupId;

    private String subject;

    @JsonProperty("rfc_message_id")
    private String rfcMessageId;

    @JsonProperty("in_reply_to")
    private String inReplyTo;

    private List<String> references;

    private String from;

    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    @JsonProperty("reply_to")
    private String replyTo;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("email_size")
    private Integer emailSize;

    @JsonProperty("text_body")
    private String textBody;

    @JsonProperty("html_body")
    private String htmlBody;

    private List<InboundAttachment> attachments;

    private InboundThreadMessageDelivery delivery;

    private List<InboundForwardOutcome> forwards;
}
