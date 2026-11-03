package io.mailtrap.api.billing;

import io.mailtrap.model.response.billing.BillingResponse;

/**
 * Interface representing the Mailtrap General API for interaction with billing
 */
public interface Billing {

    /**
     * Get current billing cycle usage for Email Testing and Email Sending
     *
     * @param accountId unique account ID
     * @return an object with current billing cycle usage for Email Testing and Email Sending if available
     */
    BillingResponse getCurrentBillingCycleUsage(long accountId);
}
