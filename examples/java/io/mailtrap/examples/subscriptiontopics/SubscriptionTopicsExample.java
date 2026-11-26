package io.mailtrap.examples.subscriptiontopics;

import io.mailtrap.api.subscriptiontopics.SubscriptionTopicListFilter;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicListResponse;

public class SubscriptionTopicsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        // The subscription topic endpoints are token-scoped: the account is resolved from the API token.
        // They are read-only: topics are created and edited in the Mailtrap app.
        final var subscriptionTopics = client.marketingApi().subscriptionTopics();

        final SubscriptionTopicListResponse page;
        try {
            // Topics by id, deleted ones left out. Pass null for the first page with API defaults.
            page = subscriptionTopics.getSubscriptionTopics(SubscriptionTopicListFilter.builder().perPage(50).build());
        } catch (final HttpClientException e) {
            if (e.getStatusCode() == 403) {
                System.out.println("Subscription topics aren't available for this account or token");
                return;
            }
            throw e;
        }

        // A topic with a null domainId covers every sending domain.
        page.getData().forEach(System.out::println);

        // Pass the next token with the same perPage to get the next page.
        if (page.getPagination().getNextToken() != null) {
            System.out.println(subscriptionTopics.getSubscriptionTopics(SubscriptionTopicListFilter.builder()
                    .perPage(50)
                    .token(page.getPagination().getNextToken())
                    .build()).getData());
        }

        if (page.getData().isEmpty()) {
            System.out.println("The account has no subscription topics yet");
            return;
        }

        // Get one topic by id. This also returns a topic deleted after a campaign was assigned it, with deletedAt set.
        System.out.println(subscriptionTopics.getSubscriptionTopic(page.getData().get(0).getId()).getData());
    }
}
