package io.mailtrap.model.response.webhooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WebhookWithSecret extends Webhook {

    @JsonProperty("signing_secret")
    private String signingSecret;

}
