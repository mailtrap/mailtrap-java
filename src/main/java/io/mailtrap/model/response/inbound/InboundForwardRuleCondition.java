package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.InboundForwardRuleMatchType;
import io.mailtrap.model.InboundForwardRuleOperator;
import lombok.Data;

@Data
public class InboundForwardRuleCondition {

    @JsonProperty("match_type")
    private InboundForwardRuleMatchType matchType;

    private InboundForwardRuleOperator operator;

    private String value;

    @JsonProperty("header_key")
    private String headerKey;
}
