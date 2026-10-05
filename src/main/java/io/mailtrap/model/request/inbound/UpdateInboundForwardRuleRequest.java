package io.mailtrap.model.request.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.mailtrap.model.AbstractModel;
import io.mailtrap.model.response.inbound.InboundForwardRuleDestination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateInboundForwardRuleRequest extends AbstractModel {

    private String name;

    private List<InboundForwardRuleConditionInput> conditions;

    private List<InboundForwardRuleDestination> destinations;
}
