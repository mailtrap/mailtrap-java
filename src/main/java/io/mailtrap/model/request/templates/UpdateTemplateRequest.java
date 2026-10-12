package io.mailtrap.model.request.templates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AbstractModel;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateTemplateRequest extends AbstractModel {

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String category;

    @Size(min = 1, max = 255)
    private String subject;

    @Size(max = 10_000_000)
    @JsonProperty("body_text")
    private String bodyText;

    @Size(max = 10_000_000)
    @JsonProperty("body_html")
    private String bodyHtml;

}
