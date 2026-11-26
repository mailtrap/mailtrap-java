package io.mailtrap.model.response.webhooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
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

    @JsonProperty("inbound_inbox_id")
    private Long inboundInboxId;

    /**
     * Event types the webhook is subscribed to. A type this SDK version doesn't know is left
     * out.
     */
    @JsonProperty("event_types")
    @JsonSetter(contentNulls = Nulls.SKIP)
    private List<WebhookEventType> eventTypes;

}
