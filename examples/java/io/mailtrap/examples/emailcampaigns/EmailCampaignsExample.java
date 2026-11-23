package io.mailtrap.examples.emailcampaigns;

import io.mailtrap.api.emailcampaigns.EmailCampaignListFilter;
import io.mailtrap.api.emailcampaigns.EmailCampaignStatsFilter;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.DeliveryMode;
import io.mailtrap.model.request.emailcampaigns.CreateEmailCampaign;
import io.mailtrap.model.request.emailcampaigns.ScheduleEmailCampaignRequest;
import io.mailtrap.model.request.emailcampaigns.TemplateAttributes;
import io.mailtrap.model.request.emailcampaigns.UpdateEmailCampaign;
import io.mailtrap.model.response.emailcampaigns.DeliveryOptions;
import io.mailtrap.model.response.emailcampaigns.ReplyTo;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopic;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class EmailCampaignsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    // ID of a verified sending domain on the account, as returned by the Sending Domains endpoints.
    private static final long DOMAIN_ID = 4321L;
    private static final long CONTACT_LIST_ID = 55L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        // The campaign endpoints are token-scoped: the account is resolved from the API token.
        final var campaigns = client.emailCampaignsApi().emailCampaigns();

        // List campaigns (newest first). Pass null for the first page with API defaults.
        final var page = campaigns.getEmailCampaigns(
            EmailCampaignListFilter.builder()
                .perPage(50)
                .search("Spring")
                .token(1)
                .build());
        System.out.println(page);

        // With subscription topics, a campaign is sent for a topic that covers its domain: here the first such
        // topic. Accounts without topics answer 403, and a campaign there has none.
        Long subscriptionTopicId = null;
        try {
            subscriptionTopicId = client.marketingApi().subscriptionTopics().getSubscriptionTopics(null).getData()
                .stream()
                .filter(topic -> topic.getDomainId() == null || topic.getDomainId() == DOMAIN_ID)
                .findFirst()
                .map(SubscriptionTopic::getId)
                .orElse(null);
        } catch (final HttpClientException e) {
            System.out.println("Subscription topics aren't available for this account: " + e.getMessage());
        }

        // Create a campaign — it starts in the `draft` state. The request body is flat.
        final var created = campaigns.createEmailCampaign(
            CreateEmailCampaign.builder()
                .name("Spring Sale")
                .domainId(DOMAIN_ID)
                .fromDisplayName("Acme Marketing")
                .fromLocalPart("news")
                .replyTo(ReplyTo.builder()
                    .displayName("Acme Support")
                    .localPart("support")
                    .domain("acme.com")
                    .build())
                .templateAttributes(TemplateAttributes.builder()
                    .subject("Spring is here — 30% off")
                    .build())
                .contactListIds(List.of(CONTACT_LIST_ID))
                // left out when null; an update can change the topic but not drop it
                .subscriptionTopicId(subscriptionTopicId)
                .build());
        System.out.println(created.getData());

        final var campaignId = created.getData().getId();

        // Retrieve a single campaign.
        final var fetched = campaigns.getEmailCampaign(campaignId);
        System.out.println(fetched.getData());

        // Update is a PATCH — only the provided fields change. The template is edited in place;
        // `bodyHtml` is the design and must contain an unsubscribe link.
        final var updated = campaigns.updateEmailCampaign(campaignId,
            UpdateEmailCampaign.builder()
                .name("Spring Sale (updated)")
                .templateAttributes(TemplateAttributes.builder()
                    .subject("New subject")
                    .bodyHtml("<html><body><h1>Hi {{first_name}}!</h1>"
                        + "<p><a href=\"__unsubscribe_url__\">Unsubscribe</a></p></body></html>")
                    .mergeTags(List.of("first_name"))
                    .build())
                .deliveryMode(DeliveryMode.GRADUAL)
                .deliveryOptions(DeliveryOptions.builder().emailsPerHour(1000).build())
                .build());
        System.out.println(updated.getData());

        // Schedule the draft to send later — the time must be in the future, at most 1 month
        // ahead; it comes back in currentStateMetadata.scheduledAt.
        final var scheduled = campaigns.scheduleEmailCampaign(campaignId,
            new ScheduleEmailCampaignRequest(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1)));
        System.out.println(scheduled.getData().getCurrentStateMetadata().getScheduledAt());

        // Cancel the scheduled send — the campaign returns to `draft`.
        final var cancelled = campaigns.cancelEmailCampaign(campaignId);
        System.out.println(cancelled.getData().getCurrentState());

        // Or start sending immediately.
        final var started = campaigns.startEmailCampaign(campaignId);
        System.out.println(started.getData().getCurrentState());

        // Aggregated performance statistics; narrow the window with start/end dates (YYYY-MM-DD).
        final var today = LocalDate.now(ZoneOffset.UTC);
        final var stats = campaigns.getEmailCampaignStats(campaignId,
            EmailCampaignStatsFilter.builder()
                .startDate(today.minusDays(30).toString())
                .endDate(today.toString())
                .build());
        System.out.println(stats.getData());

        // Only a campaign in the `draft` state can be deleted, and a started campaign never
        // returns to `draft` - so delete a fresh draft. Delete returns 204 No Content.
        final var throwaway = campaigns.createEmailCampaign(
            CreateEmailCampaign.builder()
                .name("Draft to delete")
                .domainId(DOMAIN_ID)
                .fromLocalPart("news")
                .templateAttributes(TemplateAttributes.builder()
                    .subject("Draft to delete")
                    .build())
                .build());
        campaigns.deleteEmailCampaign(throwaway.getData().getId());
    }
}
