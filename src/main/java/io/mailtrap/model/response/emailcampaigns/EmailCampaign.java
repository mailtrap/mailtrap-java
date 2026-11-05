package io.mailtrap.model.response.emailcampaigns;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.CampaignState;
import io.mailtrap.model.DeliveryMode;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * An email marketing campaign.
 *
 * <p>Returned wrapped in a {@code data} envelope by the single-campaign endpoints and as an
 * element of the list response. Some fields are conditional: the template's {@code bodyHtml}
 * and {@code bodyText} are omitted from list items, and {@code lastStartedAt}/
 * {@code recipientTotalCount} are nullable.
 */
@Data
public class EmailCampaign {

    private Long id;

    /**
     * ID of the domain used for the campaign, as returned by the Domains
     * endpoints.
     */
    @JsonProperty("domain_id")
    private Long domainId;

    @JsonProperty("domain_name")
    private String domainName;

    private String name;

    @JsonProperty("from_local_part")
    private String fromLocalPart;

    @JsonProperty("from_display_name")
    private String fromDisplayName;

    @JsonProperty("reply_to")
    private ReplyTo replyTo;

    @JsonProperty("current_state")
    private CampaignState currentState;

    @JsonProperty("current_state_metadata")
    private CurrentStateMetadata currentStateMetadata;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    @JsonProperty("last_started_at")
    private OffsetDateTime lastStartedAt;

    /**
     * Date the campaign was last started. Present only when the campaign has been started.
     */
    @JsonProperty("last_started_at_date")
    private LocalDate lastStartedAtDate;

    /**
     * Total number of recipients targeted by the campaign. {@code null} until the audience is
     * resolved.
     */
    @JsonProperty("recipient_total_count")
    private Integer recipientTotalCount;

    /**
     * IDs of the contact lists included in the campaign's audience.
     */
    @JsonProperty("contact_list_ids")
    private List<Long> contactListIds;

    /**
     * IDs of the contact segments included in the campaign's audience.
     */
    @JsonProperty("contact_segment_ids")
    private List<Long> contactSegmentIds;

    @JsonProperty("delivery_mode")
    private DeliveryMode deliveryMode;

    @JsonProperty("delivery_options")
    private DeliveryOptions deliveryOptions;

    private Template template;

}
