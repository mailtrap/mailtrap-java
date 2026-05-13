package io.mailtrap.model.response.contacts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class GetContactData {

    private String id;

    private String email;

    private Map<String, Object> fields;

    @JsonProperty("list_ids")
    private List<Long> listIds;

    private ContactStatus status;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

}
