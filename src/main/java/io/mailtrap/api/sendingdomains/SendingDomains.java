package io.mailtrap.api.sendingdomains;

import io.mailtrap.model.request.sendingdomains.CreateSendingDomainRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;
import io.mailtrap.model.request.sendingdomains.UpdateSendingDomainRequest;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;

import java.util.List;

/**
 * Interface representing the Mailtrap Sending API for interaction with domains
 */
public interface SendingDomains {

    /**
     * Create a domain
     *
     * @param accountId unique account ID
     * @param request   request data
     * @return Attributes of created domain
     */
    SendingDomainsResponse create(long accountId, CreateSendingDomainRequest request);

    /**
     * Get domains and their statuses
     *
     * @param accountId unique account ID
     * @return domains with their attributes, DNS records, statuses
     */
    List<SendingDomainsResponse> getDomains(long accountId);

    /**
     * Get domain data and it's status
     *
     * @param accountId unique account ID
     * @param domainId  unique domain ID
     * @return domain attributes, DNS records, status
     */
    SendingDomainsResponse getDomain(long accountId, long domainId);

    /**
     * Update configuration settings for a domain
     *
     * @param accountId unique account ID
     * @param domainId  unique domain ID
     * @param request   request data, only the fields set are sent
     * @return updated domain attributes, DNS records, status
     */
    SendingDomainsResponse update(long accountId, long domainId, UpdateSendingDomainRequest request);

    /**
     * Send domain setup instructions
     *
     * @param accountId unique account ID
     * @param domainId  unique domain ID
     * @param request   request data
     */
    void sendDomainsSetupInstructions(long accountId, long domainId, SendingDomainsSetupInstructionsRequest request);

    /**
     * Delete a domain
     *
     * @param accountId unique account ID
     * @param domainId  unique domain ID
     */
    void deleteDomain(long accountId, long domainId);

    /**
     * @deprecated Use {@link #getDomains(long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default List<SendingDomainsResponse> getSendingDomains(final long accountId) {
        return getDomains(accountId);
    }

    /**
     * @deprecated Use {@link #getDomain(long, long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default SendingDomainsResponse getSendingDomain(final long accountId, final long sendingDomainId) {
        return getDomain(accountId, sendingDomainId);
    }

    /**
     * @deprecated Use {@link #sendDomainsSetupInstructions(long, long, SendingDomainsSetupInstructionsRequest)} instead.
     * Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default void sendSendingDomainsSetupInstructions(final long accountId, final long sendingDomainId, final SendingDomainsSetupInstructionsRequest request) {
        sendDomainsSetupInstructions(accountId, sendingDomainId, request);
    }

    /**
     * @deprecated Use {@link #deleteDomain(long, long)} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    default void deleteSendingDomain(final long accountId, final long sendingDomainId) {
        deleteDomain(accountId, sendingDomainId);
    }
}
