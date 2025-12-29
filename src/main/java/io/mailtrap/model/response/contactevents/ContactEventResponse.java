package io.mailtrap.model.response.contactevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ContactEventResponse {

    @JsonProperty("contact_id")
    private String contactId;

    @JsonProperty("contact_email")
    private String contactEmail;

    private String name;

    private Map<String, Object> params;

}
