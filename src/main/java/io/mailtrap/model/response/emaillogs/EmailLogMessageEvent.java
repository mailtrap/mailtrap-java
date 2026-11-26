package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.OffsetDateTime;

/**
 * An event of an email log message. An event type this SDK version doesn't know reads as an
 * {@link EmailLogMessageEventUnknown}.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "event_type", visible = true,
        defaultImpl = EmailLogMessageEventUnknown.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailLogMessageEventDelivery.class, name = "delivery"),
        @JsonSubTypes.Type(value = EmailLogMessageEventOpen.class, name = "open"),
        @JsonSubTypes.Type(value = EmailLogMessageEventClick.class, name = "click"),
        @JsonSubTypes.Type(value = EmailLogMessageEventSoftBounce.class, name = "soft_bounce"),
        @JsonSubTypes.Type(value = EmailLogMessageEventBounce.class, name = "bounce"),
        @JsonSubTypes.Type(value = EmailLogMessageEventSpam.class, name = "spam"),
        @JsonSubTypes.Type(value = EmailLogMessageEventUnsubscribe.class, name = "unsubscribe"),
        @JsonSubTypes.Type(value = EmailLogMessageEventSuspension.class, name = "suspension"),
        @JsonSubTypes.Type(value = EmailLogMessageEventReject.class, name = "reject"),
        @JsonSubTypes.Type(value = EmailLogMessageEventTopicUnsubscribe.class, name = "topic_unsubscribe"),
        @JsonSubTypes.Type(value = EmailLogMessageEventTopicResubscribe.class, name = "topic_resubscribe")
})
public abstract class EmailLogMessageEvent {

    @JsonProperty("created_at")
    public OffsetDateTime createdAt;

    public abstract EmailLogEventType getEventType();

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public abstract Object getDetails();
}
