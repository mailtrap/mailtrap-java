package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * A received inbound message. The body and raw-message fields ({@code htmlBody},
 * {@code textBody}, {@code rawMessageUrl}, ...) and attachment download URLs are
 * populated on get-by-id; list items carry only the summary fields.
 */
@Data
public class InboundMessage {

    private String id;

    @JsonProperty("inbox_id")
    private Long inboxId;

    private String from;

    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    @JsonProperty("reply_to")
    private String replyTo;

    private String subject;

    @JsonProperty("rfc_message_id")
    private String rfcMessageId;

    @JsonProperty("in_reply_to")
    private String inReplyTo;

    private List<String> references;

    private Map<String, String> headers;

    private Integer size;

    @JsonProperty("html_size")
    private Integer htmlSize;

    @JsonProperty("text_size")
    private Integer textSize;

    @JsonProperty("received_at")
    private OffsetDateTime receivedAt;

    @JsonProperty("thread_id")
    private String threadId;

    private List<InboundAttachment> attachments;

    private List<InboundForwardOutcome> forwards;

    @JsonProperty("raw_message_url")
    private String rawMessageUrl;

    @JsonProperty("raw_message_expires_at")
    private OffsetDateTime rawMessageExpiresAt;

    @JsonProperty("html_body")
    private String htmlBody;

    @JsonProperty("text_body")
    private String textBody;
}
