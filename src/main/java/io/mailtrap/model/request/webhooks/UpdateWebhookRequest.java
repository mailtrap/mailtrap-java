package io.mailtrap.model.request.webhooks;

import io.mailtrap.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWebhookRequest extends AbstractModel {

    private WebhookInput webhook;

}
