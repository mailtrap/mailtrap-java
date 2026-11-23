package io.mailtrap.api.subscriptiontopics;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicListResponse;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicResponse;

import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class SubscriptionTopicsImpl extends ApiResource implements SubscriptionTopics {

    private static final String BASE_PATH = "/api/subscription_topics";

    public SubscriptionTopicsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public SubscriptionTopicListResponse getSubscriptionTopics(final SubscriptionTopicListFilter filter) {
        final var queryParams = RequestData.buildQueryParams(
            entry("per_page", Optional.ofNullable(filter).map(SubscriptionTopicListFilter::getPerPage)),
            entry("token", Optional.ofNullable(filter).map(SubscriptionTopicListFilter::getToken))
        );

        return httpClient.get(
            apiHost + BASE_PATH,
            new RequestData(queryParams),
            SubscriptionTopicListResponse.class
        );
    }

    @Override
    public SubscriptionTopicResponse getSubscriptionTopic(final long subscriptionTopicId) {
        return httpClient.get(
            String.format(apiHost + BASE_PATH + "/%d", subscriptionTopicId),
            new RequestData(),
            SubscriptionTopicResponse.class
        );
    }
}
