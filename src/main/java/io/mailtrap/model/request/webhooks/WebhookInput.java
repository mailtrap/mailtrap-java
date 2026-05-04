package io.mailtrap.model.request.webhooks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.SendingStream;
import io.mailtrap.model.WebhookEventType;
import io.mailtrap.model.WebhookPayloadFormat;
import io.mailtrap.model.WebhookType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebhookInput {

    private String url;

    @JsonProperty("webhook_type")
    private WebhookType webhookType;

    private Boolean active;

    @JsonProperty("payload_format")
    private WebhookPayloadFormat payloadFormat;

    @JsonProperty("sending_stream")
    private SendingStream sendingStream;

    @JsonProperty("event_types")
    private List<WebhookEventType> eventTypes;

    @JsonProperty("domain_id")
    private Long domainId;

}
