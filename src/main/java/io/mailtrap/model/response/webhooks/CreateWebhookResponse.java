package io.mailtrap.model.response.webhooks;

import lombok.Data;

@Data
public class CreateWebhookResponse {

    private WebhookWithSecret data;

}
