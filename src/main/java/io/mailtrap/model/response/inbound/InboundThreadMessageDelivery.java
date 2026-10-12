package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.response.emaillogs.MessageStatus;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InboundThreadMessageDelivery {

    private String to;

    private MessageStatus status;

    @JsonProperty("delivered_at")
    private OffsetDateTime deliveredAt;

    @JsonProperty("bounced_at")
    private OffsetDateTime bouncedAt;
}
