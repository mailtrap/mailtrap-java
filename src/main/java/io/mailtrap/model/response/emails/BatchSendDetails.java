package io.mailtrap.model.response.emails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BatchSendDetails {

    private boolean success;

    @JsonProperty("message_ids")
    private List<String> messageIds;

    private List<String> errors;
}
