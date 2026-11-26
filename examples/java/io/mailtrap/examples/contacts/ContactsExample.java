package io.mailtrap.examples.contacts;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.contacts.ContactSubscriptionTopic;
import io.mailtrap.model.request.contacts.CreateContact;
import io.mailtrap.model.request.contacts.CreateContactRequest;
import io.mailtrap.model.request.contacts.UpdateContact;
import io.mailtrap.model.request.contacts.UpdateContactRequest;
import io.mailtrap.model.response.contacts.ContactStatus;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;
import io.mailtrap.model.response.subscriptiontopics.SubscriptionTopic;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContactsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));
    private static final long LIST_1_ID = 1L;
    private static final long LIST_2_ID = 2L;
    private static final String EMAIL = "contact_email@email.com";
    private static final boolean UNSUBSCRIBED = false;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);

        final var createRequest = new CreateContactRequest(
            new CreateContact(EMAIL, Map.of("first_name", "Nick"), List.of(LIST_1_ID, LIST_2_ID)));

        final var createResponse = client.contactsApi().contacts()
            .createContact(ACCOUNT_ID, createRequest);

        System.out.println(createResponse);

        final var getResponse = client.contactsApi().contacts()
            .getContact(ACCOUNT_ID, EMAIL);

        System.out.println(getResponse);

        final var updateRequest = new UpdateContactRequest(
            new UpdateContact(EMAIL, Map.of("first_name", "Nick"), List.of(LIST_1_ID, LIST_2_ID),
                Collections.emptyList(), UNSUBSCRIBED));

        final var updateResponse = client.contactsApi().contacts()
            .updateContact(ACCOUNT_ID, EMAIL, updateRequest);

        System.out.println(updateResponse);

        // Unsubscribe the contact from one subscription topic on one sending domain. Entries left out keep their
        // status, and the builder leaves unsubscribed out, so the contact stays subscribed to everything else.
        firstTopicPreference(client, ContactStatus.UNSUBSCRIBED).ifPresent(preference -> {
            final var topicsUpdateRequest = new UpdateContactRequest(
                UpdateContact.builder().subscriptionTopics(List.of(preference)).build());

            // null when the account has no subscription topics, or when they couldn't be read
            System.out.println(client.contactsApi().contacts()
                .updateContact(ACCOUNT_ID, EMAIL, topicsUpdateRequest).getData().getSubscriptionTopics());
        });

        client.contactsApi().contacts().deleteContact(ACCOUNT_ID, EMAIL);
    }

    /**
     * The first subscription topic, as an entry on its sending domain or on the account's first one; empty for an
     * account without subscription topics.
     */
    private static Optional<ContactSubscriptionTopic> firstTopicPreference(final MailtrapClient client,
                                                                           final ContactStatus status) {
        final List<SubscriptionTopic> topics;
        try {
            topics = client.marketingApi().subscriptionTopics().getSubscriptionTopics(null).getData();
        } catch (final HttpClientException e) {
            System.out.println("Subscription topics aren't available for this account: " + e.getMessage());
            return Optional.empty();
        }
        if (topics.isEmpty()) {
            return Optional.empty();
        }

        final var topic = topics.get(0);
        // A topic without a domain covers every sending domain, and an entry names one of them.
        final Optional<Long> domainId = topic.getDomainId() != null
            ? Optional.of(topic.getDomainId())
            : client.sendingApi().domains().getDomains(ACCOUNT_ID).stream().findFirst().map(SendingDomainsResponse::getId);

        return domainId.map(id -> ContactSubscriptionTopic.builder()
            .subscriptionTopicId(topic.getId())
            .domainId(id)
            .status(status)
            .build());
    }
}
