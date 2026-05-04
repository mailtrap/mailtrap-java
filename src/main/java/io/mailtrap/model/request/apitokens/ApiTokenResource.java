package io.mailtrap.model.request.apitokens;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiTokenResource {

    @JsonProperty("resource_type")
    private ResourceType resourceType;

    @JsonProperty("resource_id")
    private Long resourceId;

    @JsonProperty("access_level")
    private AccessLevel accessLevel;

}
