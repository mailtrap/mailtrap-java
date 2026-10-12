package io.mailtrap.model.request.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.InboundForwardRuleMatchType;
import io.mailtrap.model.InboundForwardRuleOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InboundForwardRuleConditionInput {

    @JsonProperty("match_type")
    private InboundForwardRuleMatchType matchType;

    private InboundForwardRuleOperator operator;

    private String value;

    @JsonProperty("header_key")
    private String headerKey;
}
