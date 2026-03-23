package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDetailsReject {

    @JsonProperty("reject_reason")
    private String rejectReason;
}
