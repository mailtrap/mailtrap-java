package io.mailtrap.model.response.apitokens;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.ResourceType;
import lombok.Data;

@Data
public class ApiTokenResource {

    @JsonProperty("resource_type")
    private ResourceType resourceType;

    @JsonProperty("resource_id")
    private Long resourceId;

    @JsonProperty("access_level")
    private AccessLevel accessLevel;

}
