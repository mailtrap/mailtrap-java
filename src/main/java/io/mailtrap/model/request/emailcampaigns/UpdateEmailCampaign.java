package io.mailtrap.model.request.emailcampaigns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AbstractModel;
import io.mailtrap.model.DeliveryMode;
import io.mailtrap.model.response.emailcampaigns.DeliveryOptions;
import io.mailtrap.model.response.emailcampaigns.ReplyTo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Attributes used to update a {@code draft} email campaign. All fields are optional; only the
 * fields that are set are sent (PATCH semantics). Update accepts the same fields as create.
 */
@Data
@Builder
@NoArgsConstructor
// Private: new fields go through the builder, so the public constructors keep their signatures
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEmailCampaign extends AbstractModel {

    private String name;

    /**
     * ID of the verified domain used for the campaign, as returned by the Domains
     * endpoints.
     */
    @JsonProperty("domain_id")
    private Long domainId;

    @JsonProperty("from_display_name")
    private String fromDisplayName;

    @JsonProperty("from_local_part")
    private String fromLocalPart;

    @JsonProperty("reply_to")
    private ReplyTo replyTo;

    @JsonProperty("template_attributes")
    private TemplateAttributes templateAttributes;

    @JsonProperty("delivery_mode")
    private DeliveryMode deliveryMode;

    /**
     * Delivery throttling options. Applies when {@code deliveryMode} is
     * {@link DeliveryMode#GRADUAL}.
     */
    @JsonProperty("delivery_options")
    private DeliveryOptions deliveryOptions;

    /**
     * IDs of contact lists to send to. Treated as the full set of included lists.
     */
    @JsonProperty("contact_list_ids")
    private List<Long> contactListIds;

    /**
     * IDs of contact segments to send to. Treated as the full set of included segments.
     */
    @JsonProperty("contact_segment_ids")
    private List<Long> contactSegmentIds;

    /**
     * ID of the subscription topic the campaign is sent for, as returned by the Subscription
     * Topics API. The topic must cover the campaign's domain. Left out when {@code null}: the
     * API rejects a {@code null} topic, so a campaign can change its topic but not drop it.
     * Ignored for an account without subscription topics.
     */
    @JsonProperty("subscription_topic_id")
    private Long subscriptionTopicId;

    /**
     * The constructor of previous versions, without {@code subscriptionTopicId}.
     */
    public UpdateEmailCampaign(final String name, final Long domainId, final String fromDisplayName, final String fromLocalPart,
            final ReplyTo replyTo, final TemplateAttributes templateAttributes, final DeliveryMode deliveryMode,
            final DeliveryOptions deliveryOptions, final List<Long> contactListIds, final List<Long> contactSegmentIds) {
        this(name, domainId, fromDisplayName, fromLocalPart, replyTo, templateAttributes, deliveryMode, deliveryOptions,
            contactListIds, contactSegmentIds, null);
    }

}
