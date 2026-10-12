package io.mailtrap.api.inbound;

import io.mailtrap.model.request.inbound.CreateInboundForwardRuleRequest;
import io.mailtrap.model.request.inbound.UpdateInboundForwardRuleRequest;
import io.mailtrap.model.response.inbound.InboundForwardRule;

import java.util.List;

/**
 * Interface representing the Mailtrap Inbound Email API for managing forward rules.
 */
public interface InboundForwardRules {

    /**
     * List forward rules in an inbox.
     *
     * @param inboxId the inbox ID
     * @return the list of forward rules
     */
    List<InboundForwardRule> getList(long inboxId);

    /**
     * Get a forward rule by ID.
     *
     * @param inboxId the inbox ID
     * @param ruleId  the forward rule ID
     * @return the forward rule
     */
    InboundForwardRule getById(long inboxId, long ruleId);

    /**
     * Create a new forward rule.
     *
     * @param inboxId the inbox ID
     * @param request the create request
     * @return the created forward rule
     */
    InboundForwardRule create(long inboxId, CreateInboundForwardRuleRequest request);

    /**
     * Update a forward rule.
     *
     * @param inboxId the inbox ID
     * @param ruleId  the forward rule ID
     * @param request the update request
     * @return the updated forward rule
     */
    InboundForwardRule update(long inboxId, long ruleId, UpdateInboundForwardRuleRequest request);

    /**
     * Delete a forward rule.
     *
     * @param inboxId the inbox ID
     * @param ruleId  the forward rule ID
     */
    void delete(long inboxId, long ruleId);
}
