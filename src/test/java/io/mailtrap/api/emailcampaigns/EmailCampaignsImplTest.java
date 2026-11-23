package io.mailtrap.api.emailcampaigns;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.CampaignState;
import io.mailtrap.model.DeliveryMode;
import io.mailtrap.model.request.emailcampaigns.CreateEmailCampaign;
import io.mailtrap.model.request.emailcampaigns.ScheduleEmailCampaignRequest;
import io.mailtrap.model.request.emailcampaigns.TemplateAttributes;
import io.mailtrap.model.request.emailcampaigns.UpdateEmailCampaign;
import io.mailtrap.model.response.emailcampaigns.DeliveryOptions;
import io.mailtrap.model.response.emailcampaigns.EmailCampaign;
import io.mailtrap.model.response.emailcampaigns.EmailCampaignListResponse;
import io.mailtrap.model.response.emailcampaigns.EmailCampaignResponse;
import io.mailtrap.model.response.emailcampaigns.EmailCampaignStatsResponse;
import io.mailtrap.model.response.emailcampaigns.ReplyTo;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmailCampaignsImplTest extends BaseTest {

    private static final String BODY_HTML =
            "<html><body><h1>Hi {{first_name}}!</h1><p><a href=\"__unsubscribe_url__\">Unsubscribe</a></p></body></html>";

    private final long emailCampaignId = 4567L;
    private final long domainId = 4321L;

    private EmailCampaigns api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns",
                        "GET", null, "api/emailcampaigns/listEmailCampaignsResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns",
                        "GET", null, "api/emailcampaigns/listEmailCampaignsResponse.json", Map.of("search", "Spring")),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns",
                        "POST", "api/emailcampaigns/createEmailCampaignRequest.json", "api/emailcampaigns/createEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId,
                        "GET", null, "api/emailcampaigns/getEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId,
                        "PATCH", "api/emailcampaigns/updateEmailCampaignRequest.json", "api/emailcampaigns/updateEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId,
                        "DELETE", null, null),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/start",
                        "POST", null, "api/emailcampaigns/startEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/schedule",
                        "POST", "api/emailcampaigns/scheduleEmailCampaignRequest.json", "api/emailcampaigns/scheduleEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/cancel",
                        "POST", null, "api/emailcampaigns/cancelEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/terminate",
                        "POST", null, "api/emailcampaigns/terminateEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/reset",
                        "POST", null, "api/emailcampaigns/resetEmailCampaignResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/stats",
                        "GET", null, "api/emailcampaigns/getEmailCampaignStatsResponse.json"),

                DataMock.build(Constants.GENERAL_HOST + "/api/email_campaigns/" + emailCampaignId + "/stats",
                        "GET", null, "api/emailcampaigns/getEmailCampaignStatsResponse.json",
                        Map.of("start_date", "2026-05-01", "end_date", "2026-05-31"))
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).emailCampaignsApi().emailCampaigns();
    }

    @Test
    void test_getEmailCampaigns() {
        final EmailCampaignListResponse response = api.getEmailCampaigns(null);

        assertNotNull(response);
        assertEquals(2, response.getData().size());

        final EmailCampaign first = response.getData().get(0);
        assertEquals(emailCampaignId, first.getId());
        assertEquals(domainId, first.getDomainId());
        assertEquals(CampaignState.DRAFT, first.getCurrentState());
        assertEquals(DeliveryMode.RAPID, first.getDeliveryMode());
        assertEquals(List.of(55L, 56L), first.getContactListIds());
        assertEquals(List.of(12L), first.getContactSegmentIds());
        assertEquals(77L, first.getSubscriptionTopicId());
        // template bodies are omitted from list responses
        assertNull(first.getTemplate().getBodyHtml());
        assertEquals(List.of("first_name"), first.getTemplate().getMergeTags());

        final EmailCampaign second = response.getData().get(1);
        assertEquals(CampaignState.FAILED, second.getCurrentState());
        assertNull(second.getSubscriptionTopicId());
        assertEquals("Invalid recipient address", second.getCurrentStateMetadata().getErrors().get(0).getMessage());
        assertEquals(0, second.getCurrentStateMetadata().getErrors().get(0).getRcptIndex());

        assertNotNull(response.getPagination());
        assertEquals(1, response.getPagination().getToken());
        assertNull(response.getPagination().getPrevToken());
        assertEquals(2, response.getPagination().getNextToken());
    }

    @Test
    void test_getEmailCampaigns_filtersBySearch() {
        final EmailCampaignListResponse response = api.getEmailCampaigns(EmailCampaignListFilter.builder().search("Spring").build());

        assertNotNull(response);
        assertEquals(2, response.getData().size());
    }

    @Test
    void test_createEmailCampaign() {
        final CreateEmailCampaign request = CreateEmailCampaign.builder()
                .name("Spring Sale")
                .domainId(domainId)
                .fromDisplayName("Acme Marketing")
                .fromLocalPart("news")
                .replyTo(ReplyTo.builder()
                        .displayName("Acme Support")
                        .localPart("support")
                        .domain("acme.com")
                        .build())
                .templateAttributes(TemplateAttributes.builder()
                        .subject("Spring is here — 30% off")
                        .bodyHtml(BODY_HTML)
                        .mergeTags(List.of("first_name"))
                        .build())
                .deliveryMode(DeliveryMode.RAPID)
                .contactListIds(List.of(55L, 56L))
                .contactSegmentIds(List.of(12L))
                .subscriptionTopicId(77L)
                .build();

        final EmailCampaignResponse response = api.createEmailCampaign(request);

        assertNotNull(response);
        final EmailCampaign campaign = response.getData();
        assertEquals(emailCampaignId, campaign.getId());
        assertEquals("Spring Sale", campaign.getName());
        assertEquals(domainId, campaign.getDomainId());
        assertEquals(CampaignState.DRAFT, campaign.getCurrentState());
        assertEquals(DeliveryMode.RAPID, campaign.getDeliveryMode());
        assertEquals(789L, campaign.getTemplate().getId());
        assertEquals(BODY_HTML, campaign.getTemplate().getBodyHtml());
        assertEquals(77L, campaign.getSubscriptionTopicId());
        // audience is resolved asynchronously
        assertNull(campaign.getRecipientTotalCount());
    }

    @Test
    void test_getEmailCampaign() {
        final EmailCampaignResponse response = api.getEmailCampaign(emailCampaignId);

        assertNotNull(response);
        final EmailCampaign campaign = response.getData();
        assertEquals(emailCampaignId, campaign.getId());
        assertEquals("acme.com", campaign.getDomainName());
        assertEquals("Acme Support", campaign.getReplyTo().getDisplayName());
        assertEquals(DeliveryMode.GRADUAL, campaign.getDeliveryMode());
        assertEquals(1000, campaign.getDeliveryOptions().getEmailsPerHour());
        assertEquals(1500, campaign.getRecipientTotalCount());
        assertEquals("Hi {{first_name}}! Unsubscribe: __unsubscribe_url__", campaign.getTemplate().getBodyText());
    }

    @Test
    void test_updateEmailCampaign() {
        final UpdateEmailCampaign request = UpdateEmailCampaign.builder()
                .name("Spring Sale (updated)")
                .templateAttributes(TemplateAttributes.builder()
                        .subject("New subject")
                        .bodyHtml(BODY_HTML)
                        .mergeTags(List.of("first_name"))
                        .build())
                .deliveryMode(DeliveryMode.GRADUAL)
                .deliveryOptions(DeliveryOptions.builder().emailsPerHour(1000).build())
                .contactListIds(List.of(55L, 56L))
                .contactSegmentIds(List.of(12L))
                .subscriptionTopicId(78L)
                .build();

        final EmailCampaignResponse response = api.updateEmailCampaign(emailCampaignId, request);

        assertNotNull(response);
        final EmailCampaign campaign = response.getData();
        assertEquals(emailCampaignId, campaign.getId());
        assertEquals("Spring Sale (updated)", campaign.getName());
        assertEquals(CampaignState.DRAFT, campaign.getCurrentState());
        assertEquals(DeliveryMode.GRADUAL, campaign.getDeliveryMode());
        assertEquals("New subject", campaign.getTemplate().getSubject());
        assertEquals(78L, campaign.getSubscriptionTopicId());
    }

    @Test
    void test_campaignRequests_leaveOutAnUnsetSubscriptionTopic() {
        assertFalse(CreateEmailCampaign.builder().name("Spring Sale").build().toJson().contains("subscription_topic_id"));
        assertFalse(UpdateEmailCampaign.builder().name("Spring Sale").build().toJson().contains("subscription_topic_id"));
        // the constructors of previous versions leave it unset
        assertNull(new CreateEmailCampaign("Spring Sale", domainId, null, "news", null, null, null, null, null, null)
                .getSubscriptionTopicId());
        assertNull(new UpdateEmailCampaign("Spring Sale", null, null, null, null, null, null, null, null, null)
                .getSubscriptionTopicId());
    }

    @Test
    void test_deleteEmailCampaign() {
        // delete returns 204 No Content with no body
        assertDoesNotThrow(() -> api.deleteEmailCampaign(emailCampaignId));
    }

    @Test
    void test_startEmailCampaign() {
        final EmailCampaignResponse response = api.startEmailCampaign(emailCampaignId);

        assertNotNull(response);
        assertEquals(CampaignState.STARTED, response.getData().getCurrentState());
        assertNotNull(response.getData().getLastStartedAt());
    }

    @Test
    void test_scheduleEmailCampaign() {
        final ScheduleEmailCampaignRequest request = new ScheduleEmailCampaignRequest(
                OffsetDateTime.of(2026, 6, 1, 9, 0, 0, 0, ZoneOffset.UTC));

        final EmailCampaignResponse response = api.scheduleEmailCampaign(emailCampaignId, request);

        assertNotNull(response);
        assertEquals(CampaignState.SCHEDULED, response.getData().getCurrentState());
        assertEquals(OffsetDateTime.of(2026, 6, 1, 9, 0, 0, 0, ZoneOffset.UTC),
                response.getData().getCurrentStateMetadata().getScheduledAt());
    }

    @Test
    void test_cancelEmailCampaign() {
        final EmailCampaignResponse response = api.cancelEmailCampaign(emailCampaignId);

        assertNotNull(response);
        assertEquals(CampaignState.DRAFT, response.getData().getCurrentState());
    }

    @Test
    void test_terminateEmailCampaign() {
        final EmailCampaignResponse response = api.terminateEmailCampaign(emailCampaignId);

        assertNotNull(response);
        assertEquals(CampaignState.TERMINATING, response.getData().getCurrentState());
    }

    @Test
    void test_resetEmailCampaign() {
        final EmailCampaignResponse response = api.resetEmailCampaign(emailCampaignId);

        assertNotNull(response);
        assertEquals(CampaignState.DRAFT, response.getData().getCurrentState());
    }

    @Test
    void test_getEmailCampaignStats() {
        final EmailCampaignStatsResponse response = api.getEmailCampaignStats(emailCampaignId, null);

        assertNotNull(response);
        assertEquals(1450, response.getData().getDeliveryCount());
        assertEquals(820, response.getData().getOpenCount());
        assertEquals(0.9667, response.getData().getDeliveryRate());
        assertEquals(0.5655, response.getData().getOpenRate());
    }

    @Test
    void test_getEmailCampaignStats_withDateWindow() {
        final EmailCampaignStatsResponse response =
                api.getEmailCampaignStats(emailCampaignId,
                        EmailCampaignStatsFilter.builder().startDate("2026-05-01").endDate("2026-05-31").build());

        assertNotNull(response);
        assertEquals(1450, response.getData().getDeliveryCount());
    }
}
