package io.mailtrap.examples.inbound;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.InboundForwardRuleMatchType;
import io.mailtrap.model.InboundForwardRuleOperator;
import io.mailtrap.model.request.inbound.CreateInboundForwardRuleRequest;
import io.mailtrap.model.request.inbound.InboundForwardRuleConditionInput;
import io.mailtrap.model.request.inbound.UpdateInboundForwardRuleRequest;
import io.mailtrap.model.response.inbound.InboundForwardRuleDestination;

import java.util.List;

public class InboundForwardRulesExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long INBOX_ID = Long.parseLong(System.getenv("MAILTRAP_INBOUND_INBOX_ID"));

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);
        final var forwardRules = client.inboundApi().forwardRules();

        // List forward rules in an inbox.
        final var allRules = forwardRules.getList(INBOX_ID);
        System.out.println(allRules);

        // Create a forward rule.
        final var created = forwardRules.create(INBOX_ID, CreateInboundForwardRuleRequest.builder()
                .name("Copy billing mail to finance")
                .conditions(List.of(InboundForwardRuleConditionInput.builder()
                        .matchType(InboundForwardRuleMatchType.SENDER)
                        .operator(InboundForwardRuleOperator.ENDS_WITH)
                        .value("@billing.example.com")
                        .build()))
                .destinations(List.of(new InboundForwardRuleDestination("finance@example.com")))
                .build());
        System.out.println(created);

        // Get a forward rule by ID.
        final var rule = forwardRules.getById(INBOX_ID, created.getId());
        System.out.println(rule);

        // Update a forward rule.
        final var updated = forwardRules.update(INBOX_ID, created.getId(), UpdateInboundForwardRuleRequest.builder()
                .destinations(List.of(
                        new InboundForwardRuleDestination("finance@example.com"),
                        new InboundForwardRuleDestination("accounting@example.com")))
                .build());
        System.out.println(updated);

        // Delete a forward rule.
        forwardRules.delete(INBOX_ID, created.getId());
    }
}
