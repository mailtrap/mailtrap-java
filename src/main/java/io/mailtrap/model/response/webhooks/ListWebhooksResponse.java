package io.mailtrap.model.response.webhooks;

import lombok.Data;

import java.util.List;

@Data
public class ListWebhooksResponse {

    private List<Webhook> data;

}
