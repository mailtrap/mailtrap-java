package io.mailtrap.model.response.apitokens;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ApiToken {

    private long id;

    private String name;

    @JsonProperty("last_4_digits")
    private String last4Digits;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("expires_at")
    private OffsetDateTime expiresAt;

    private List<ApiTokenResource> resources;

}
