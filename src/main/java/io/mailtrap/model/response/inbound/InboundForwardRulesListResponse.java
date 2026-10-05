package io.mailtrap.model.response.inbound;

import lombok.Data;

import java.util.List;

@Data
public class InboundForwardRulesListResponse {

    private List<InboundForwardRule> data;
}
