package io.mailtrap.api.billing;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.billing.BillingResponse;

public class BillingImpl extends ApiResource implements Billing {

    public BillingImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public BillingResponse getCurrentBillingCycleUsage(final long accountId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/billing/usage", accountId),
            new RequestData(),
            BillingResponse.class);
    }
}
