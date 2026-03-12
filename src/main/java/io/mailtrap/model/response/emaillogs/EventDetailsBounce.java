package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDetailsBounce {

    @JsonProperty("sending_ip")
    private String sendingIp;

    @JsonProperty("recipient_mx")
    private String recipientMx;

    @JsonProperty("email_service_provider")
    private String emailServiceProvider;

    @JsonProperty("email_service_provider_status")
    private String emailServiceProviderStatus;

    @JsonProperty("email_service_provider_response")
    private String emailServiceProviderResponse;

    @JsonProperty("bounce_category")
    private String bounceCategory;
}
