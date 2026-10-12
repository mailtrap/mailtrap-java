package io.mailtrap.api.inbound;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.InboundForwardRuleMatchType;
import io.mailtrap.model.InboundForwardRuleOperator;
import io.mailtrap.model.request.inbound.CreateInboundForwardRuleRequest;
import io.mailtrap.model.request.inbound.InboundForwardRuleConditionInput;
import io.mailtrap.model.request.inbound.UpdateInboundForwardRuleRequest;
import io.mailtrap.model.response.inbound.InboundForwardRule;
import io.mailtrap.model.response.inbound.InboundForwardRuleDestination;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InboundForwardRulesImplTest extends BaseTest {

    private static final long INBOX_ID = 201L;
    private static final long RULE_ID = 7L;
    private static final long OTHER_RULE_ID = 9L;

    private InboundForwardRules api;

    @BeforeEach
    void init() {
        final String rulesUrl = Constants.GENERAL_HOST + "/api/inbound/inboxes/" + INBOX_ID + "/forward_rules";
        final String ruleUrl = rulesUrl + "/" + RULE_ID;

        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(rulesUrl, "GET", null, "api/inbound/listInboundForwardRulesResponse.json"),
                DataMock.build(rulesUrl + "/" + OTHER_RULE_ID, "GET", null,
                        "api/inbound/getInboundForwardRuleResponse.json"),
                DataMock.build(rulesUrl, "POST", "api/inbound/createInboundForwardRuleRequest.json",
                        "api/inbound/createInboundForwardRuleResponse.json"),
                DataMock.build(ruleUrl, "PATCH", "api/inbound/updateInboundForwardRuleRequest.json",
                        "api/inbound/updateInboundForwardRuleResponse.json"),
                DataMock.build(ruleUrl, "PATCH", "api/inbound/clearInboundForwardRuleConditionsRequest.json",
                        "api/inbound/clearInboundForwardRuleConditionsResponse.json"),
                DataMock.build(ruleUrl, "DELETE", null, null)
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).inboundApi().forwardRules();
    }

    @Test
    void getList_returnsRules() {
        final List<InboundForwardRule> rules = api.getList(INBOX_ID);

        assertNotNull(rules);
        assertEquals(2, rules.size());

        final InboundForwardRule first = rules.get(0);
        assertEquals(7, first.getId());
        assertEquals("Copy billing mail to finance", first.getName());
        assertEquals(OffsetDateTime.parse("2026-05-08T10:30:00.000Z"), first.getCreatedAt());
        assertEquals(1, first.getConditions().size());
        assertEquals(InboundForwardRuleMatchType.SENDER, first.getConditions().get(0).getMatchType());
        assertEquals(InboundForwardRuleOperator.ENDS_WITH, first.getConditions().get(0).getOperator());
        assertEquals("@billing.example.com", first.getConditions().get(0).getValue());
        assertNull(first.getConditions().get(0).getHeaderKey());
        assertEquals(List.of(new InboundForwardRuleDestination("finance@example.com")), first.getDestinations());

        assertTrue(rules.get(1).getConditions().isEmpty());
    }

    @Test
    void getById_returnsRule() {
        final InboundForwardRule rule = api.getById(INBOX_ID, OTHER_RULE_ID);

        assertNotNull(rule);
        assertEquals(9, rule.getId());
        assertEquals("Escalate urgent tickets", rule.getName());
        assertEquals(OffsetDateTime.parse("2026-05-08T12:15:00.000Z"), rule.getUpdatedAt());
        assertEquals(2, rule.getConditions().size());
        assertEquals(InboundForwardRuleMatchType.HEADER, rule.getConditions().get(0).getMatchType());
        assertEquals(InboundForwardRuleOperator.EQUAL, rule.getConditions().get(0).getOperator());
        assertEquals("X-Priority-Level", rule.getConditions().get(0).getHeaderKey());
        assertEquals(InboundForwardRuleMatchType.RECIPIENT, rule.getConditions().get(1).getMatchType());
        assertEquals(InboundForwardRuleOperator.STARTS_WITH, rule.getConditions().get(1).getOperator());
        assertEquals(2, rule.getDestinations().size());
        assertEquals("lead@example.com", rule.getDestinations().get(1).getEmail());
    }

    @Test
    void create_returnsCreatedRule() {
        final CreateInboundForwardRuleRequest request = CreateInboundForwardRuleRequest.builder()
                .name("Copy billing mail to finance")
                .conditions(List.of(
                        InboundForwardRuleConditionInput.builder()
                                .matchType(InboundForwardRuleMatchType.SENDER)
                                .operator(InboundForwardRuleOperator.ENDS_WITH)
                                .value("@billing.example.com")
                                .build(),
                        InboundForwardRuleConditionInput.builder()
                                .matchType(InboundForwardRuleMatchType.HEADER)
                                .operator(InboundForwardRuleOperator.NOT_EMPTY)
                                .headerKey("X-Invoice-Id")
                                .build()))
                .destinations(List.of(new InboundForwardRuleDestination("finance@example.com")))
                .build();

        final InboundForwardRule rule = api.create(INBOX_ID, request);

        assertNotNull(rule);
        assertEquals(7, rule.getId());
        assertEquals(2, rule.getConditions().size());
        assertEquals(InboundForwardRuleOperator.NOT_EMPTY, rule.getConditions().get(1).getOperator());
        assertNull(rule.getConditions().get(1).getValue());
        assertEquals("finance@example.com", rule.getDestinations().get(0).getEmail());
    }

    @Test
    void update_sendsOnlySetFields() {
        final UpdateInboundForwardRuleRequest request = UpdateInboundForwardRuleRequest.builder()
                .destinations(List.of(
                        new InboundForwardRuleDestination("finance@example.com"),
                        new InboundForwardRuleDestination("accounting@example.com")))
                .build();

        assertEquals(
                "{\"destinations\":[{\"email\":\"finance@example.com\"},{\"email\":\"accounting@example.com\"}]}",
                request.toJson());

        final InboundForwardRule rule = api.update(INBOX_ID, RULE_ID, request);

        assertNotNull(rule);
        assertEquals(7, rule.getId());
        assertEquals(2, rule.getDestinations().size());
        assertEquals("accounting@example.com", rule.getDestinations().get(1).getEmail());
    }

    @Test
    void update_withEmptyConditions_sendsEmptyArray() {
        final UpdateInboundForwardRuleRequest request = UpdateInboundForwardRuleRequest.builder()
                .conditions(List.of())
                .build();

        assertEquals("{\"conditions\":[]}", request.toJson());

        final InboundForwardRule rule = api.update(INBOX_ID, RULE_ID, request);

        assertNotNull(rule);
        assertTrue(rule.getConditions().isEmpty());
    }

    @Test
    void delete_doesNotThrow() {
        assertDoesNotThrow(() -> api.delete(INBOX_ID, RULE_ID));
    }
}
