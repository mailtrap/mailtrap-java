package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.SendingStream;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;

@Data
public class EmailLogMessageSummary {

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("status")
    private MessageStatus status;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("sent_at")
    private OffsetDateTime sentAt;

    @JsonProperty("client_ip")
    private String clientIp;

    @JsonProperty("category")
    private String category;

    @JsonProperty("custom_variables")
    private Map<String, Object> customVariables;

    @JsonProperty("sending_stream")
    private SendingStream sendingStream;

    @JsonProperty("sending_domain_id")
    private Integer sendingDomainId;

    @JsonProperty("template_id")
    private Integer templateId;

    @JsonProperty("template_variables")
    private Map<String, Object> templateVariables;

    @JsonProperty("opens_count")
    private Integer opensCount;

    @JsonProperty("clicks_count")
    private Integer clicksCount;

    /**
     * Returns custom variables; never null (empty map if not set).
     */
    public Map<String, Object> getCustomVariables() {
        return customVariables == null ? Collections.emptyMap() : customVariables;
    }

    /**
     * Returns template variables; never null (empty map if not set).
     */
    public Map<String, Object> getTemplateVariables() {
        return templateVariables == null ? Collections.emptyMap() : templateVariables;
    }
}
