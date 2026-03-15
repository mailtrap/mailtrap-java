package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDetailsDelivery {

    @JsonProperty("sending_ip")
    private String sendingIp;

    @JsonProperty("recipient_mx")
    private String recipientMx;

    @JsonProperty("email_service_provider")
    private String emailServiceProvider;
}
