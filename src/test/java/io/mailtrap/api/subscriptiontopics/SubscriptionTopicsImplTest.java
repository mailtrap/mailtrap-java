package io.mailtrap.api.subscriptiontopics;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopic;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicListResponse;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopicResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubscriptionTopicsImplTest extends BaseTest {

    private final String url = Constants.GENERAL_HOST + "/api/subscription_topics";
    private final long deletedTopicId = 76L;

    private SubscriptionTopics api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(url, "GET", null, "api/subscriptiontopics/listSubscriptionTopicsResponse.json"),

            DataMock.build(url, "GET", null, "api/subscriptiontopics/listSubscriptionTopicsResponse.json",
                Map.of("per_page", 2, "token", 2)),

            DataMock.build(url + "/" + deletedTopicId, "GET", null, "api/subscriptiontopics/getSubscriptionTopicResponse.json")
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).marketingApi().subscriptionTopics();
    }

    @Test
    void test_getSubscriptionTopics() {
        final SubscriptionTopicListResponse response = api.getSubscriptionTopics(null);

        assertNotNull(response);
        assertEquals(2, response.getData().size());

        final SubscriptionTopic first = response.getData().get(0);
        assertEquals(77L, first.getId());
        assertEquals("Weekly newsletter", first.getName());
        assertEquals("Our best articles, every Monday", first.getDescription());
        assertEquals(4321L, first.getDomainId());
        assertNull(first.getDeletedAt());
        assertEquals(OffsetDateTime.of(2026, 10, 1, 9, 0, 0, 0, ZoneOffset.UTC), first.getCreatedAt());

        // a topic of every domain
        final SubscriptionTopic second = response.getData().get(1);
        assertNull(second.getDomainId());
        assertNull(second.getDescription());
    }

    @Test
    void test_getSubscriptionTopics_paginated() {
        final SubscriptionTopicListResponse response = api.getSubscriptionTopics(
            SubscriptionTopicListFilter.builder().perPage(2).token(2).build());

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(2, response.getPagination().getToken());
        assertEquals(1, response.getPagination().getPrevToken());
        assertEquals(3, response.getPagination().getNextToken());
    }

    @Test
    void test_getSubscriptionTopic_deleted() {
        final SubscriptionTopicResponse response = api.getSubscriptionTopic(deletedTopicId);

        assertNotNull(response);
        final SubscriptionTopic topic = response.getData();
        assertEquals(deletedTopicId, topic.getId());
        assertEquals("Spring promotions", topic.getName());
        assertEquals(4321L, topic.getDomainId());
        assertEquals(OffsetDateTime.of(2026, 11, 1, 12, 30, 0, 0, ZoneOffset.UTC), topic.getDeletedAt());
    }
}
