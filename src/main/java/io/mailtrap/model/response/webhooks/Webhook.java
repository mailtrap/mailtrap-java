package io.mailtrap.model.response.webhooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.SendingStream;
import io.mailtrap.model.WebhookEventType;
import io.mailtrap.model.WebhookPayloadFormat;
import io.mailtrap.model.WebhookType;
import lombok.Data;

import java.util.List;

@Data
public class Webhook {

    private long id;

    private String url;

    private boolean active;

    @JsonProperty("webhook_type")
    private WebhookType webhookType;

    @JsonProperty("payload_format")
    private WebhookPayloadFormat payloadFormat;

    @JsonProperty("sending_stream")
    private SendingStream sendingStream;

    @JsonProperty("domain_id")
    private Long domainId;

    @JsonProperty("event_types")
    private List<WebhookEventType> eventTypes;

}
