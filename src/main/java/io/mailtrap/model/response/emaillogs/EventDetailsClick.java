package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDetailsClick {

    @JsonProperty("click_url")
    private String clickUrl;

    @JsonProperty("web_ip_address")
    private String webIpAddress;
}
