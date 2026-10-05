package io.mailtrap.model.response.templates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Template {

    private long id;

    private String uuid;

    private String name;

    private String category;

    private String subject;

    @JsonProperty("body_text")
    private String bodyText;

    @JsonProperty("body_html")
    private String bodyHtml;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

}
