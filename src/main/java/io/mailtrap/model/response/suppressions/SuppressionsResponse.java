package io.mailtrap.model.response.suppressions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.SendingStream;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SuppressionsResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private SuppressionType type;

  @JsonProperty("created_at")
  private OffsetDateTime createdAt;

  @JsonProperty("email")
  private String email;

  @JsonProperty("sending_stream")
  private SendingStream sendingStream;

  @JsonProperty("domain_name")
  private String domainName;

  @JsonProperty("message_bounce_category")
  private String messageBounceCategory;

  @JsonProperty("message_category")
  private String messageCategory;

  @JsonProperty("message_client_ip")
  private String messageClientIp;

  @JsonProperty("message_created_at")
  private OffsetDateTime messageCreatedAt;

  @JsonProperty("message_outgoing_ip")
  private String messageOutgoingIp;

  @JsonProperty("message_recipient_mx_name")
  private String messageRecipientMxName;

  @JsonProperty("message_sender_email")
  private String messageSenderEmail;

  @JsonProperty("message_subject")
  private String messageSubject;
}
