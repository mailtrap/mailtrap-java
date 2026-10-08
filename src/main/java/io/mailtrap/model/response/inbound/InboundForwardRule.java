package io.mailtrap.model.response.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class InboundForwardRule {

    private long id;

    private String name;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    private List<InboundForwardRuleCondition> conditions;

    private List<InboundForwardRuleDestination> destinations;
}
