package io.mailtrap.api.inbound;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.inbound.CreateInboundForwardRuleRequest;
import io.mailtrap.model.request.inbound.UpdateInboundForwardRuleRequest;
import io.mailtrap.model.response.inbound.InboundForwardRule;
import io.mailtrap.model.response.inbound.InboundForwardRuleResponse;
import io.mailtrap.model.response.inbound.InboundForwardRulesListResponse;

import java.util.List;

public class InboundForwardRulesImpl extends ApiResource implements InboundForwardRules {

    public InboundForwardRulesImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public List<InboundForwardRule> getList(final long inboxId) {
        return httpClient.get(
            String.format(apiHost + "/api/inbound/inboxes/%d/forward_rules", inboxId),
            new RequestData(),
            InboundForwardRulesListResponse.class
        ).getData();
    }

    @Override
    public InboundForwardRule getById(final long inboxId, final long ruleId) {
        return httpClient.get(
            String.format(apiHost + "/api/inbound/inboxes/%d/forward_rules/%d", inboxId, ruleId),
            new RequestData(),
            InboundForwardRuleResponse.class
        ).getData();
    }

    @Override
    public InboundForwardRule create(final long inboxId, final CreateInboundForwardRuleRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/inbound/inboxes/%d/forward_rules", inboxId),
            request,
            new RequestData(),
            InboundForwardRuleResponse.class
        ).getData();
    }

    @Override
    public InboundForwardRule update(final long inboxId, final long ruleId,
                                     final UpdateInboundForwardRuleRequest request) {
        return httpClient.patch(
            String.format(apiHost + "/api/inbound/inboxes/%d/forward_rules/%d", inboxId, ruleId),
            request,
            new RequestData(),
            InboundForwardRuleResponse.class
        ).getData();
    }

    @Override
    public void delete(final long inboxId, final long ruleId) {
        httpClient.delete(
            String.format(apiHost + "/api/inbound/inboxes/%d/forward_rules/%d", inboxId, ruleId),
            new RequestData(),
            Void.class
        );
    }
}
