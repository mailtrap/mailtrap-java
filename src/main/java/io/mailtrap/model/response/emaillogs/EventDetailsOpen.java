package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDetailsOpen {

    @JsonProperty("web_ip_address")
    private String webIpAddress;
}
