package io.mailtrap.model.response.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundForwardRuleDestination {

    private String email;
}
