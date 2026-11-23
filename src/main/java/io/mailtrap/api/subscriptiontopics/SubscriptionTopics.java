package io.mailtrap.api.subscriptiontopics;

import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicListResponse;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicResponse;

/**
 * Subscription Topics API. Read-only: topics are managed in the Mailtrap app, and their ids are
 * what campaigns ({@code subscriptionTopicId}) and contacts ({@code subscriptionTopics}) refer to.
 *
 * <p>These endpoints are token-scoped: the account is resolved from the API token, so the
 * path takes no account id. For an account without subscription topics they fail with an
 * {@link io.mailtrap.exception.http.HttpClientException} of status {@code 403}.
 */
public interface SubscriptionTopics {

    /**
     * List the account's subscription topics, by id. Deleted topics are left out.
     *
     * @param filter pagination parameters; {@code null} for the first page with API defaults
     * @return a page of topics and the pagination metadata; pass
     * {@code getPagination().getNextToken()} with the same {@code perPage} to get the next page
     */
    SubscriptionTopicListResponse getSubscriptionTopics(SubscriptionTopicListFilter filter);

    /**
     * Get a subscription topic by ID, including a deleted one ({@code deletedAt} is then set):
     * a campaign keeps a topic that was deleted after it was assigned.
     *
     * @param subscriptionTopicId unique subscription topic ID
     * @return the subscription topic
     */
    SubscriptionTopicResponse getSubscriptionTopic(long subscriptionTopicId);
}
