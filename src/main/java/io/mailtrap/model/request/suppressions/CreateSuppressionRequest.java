package io.mailtrap.model.request.suppressions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AbstractModel;
import io.mailtrap.model.SendingStream;
import io.mailtrap.model.response.suppressions.SuppressionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateSuppressionRequest extends AbstractModel {

    private String email;

    @JsonProperty("domain_id")
    private Long domainId;

    @JsonProperty("sending_stream")
    private SendingStream sendingStream;

    /**
     * Reason for the suppression. The API defaults to {@code manual import} when omitted, and
     * rejects {@link SuppressionType#TOPIC_UNSUBSCRIPTION}.
     */
    private SuppressionType type;

}
