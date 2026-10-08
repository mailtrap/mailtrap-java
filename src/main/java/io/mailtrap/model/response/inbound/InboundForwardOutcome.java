package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InboundForwardOutcome {

    @JsonProperty("rule_id")
    private Long ruleId;

    @JsonProperty("rule_name")
    private String ruleName;

    private String destination;

    private InboundForwardOutcomeStatus status;

    private String reason;

    @JsonProperty("message_id")
    private String messageId;
}
