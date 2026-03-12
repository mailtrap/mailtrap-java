package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDetailsSpam {

    @JsonProperty("spam_feedback_type")
    private String spamFeedbackType;
}
