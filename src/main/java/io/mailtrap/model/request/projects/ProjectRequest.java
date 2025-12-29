package io.mailtrap.model.request.projects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AbstractModel;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectRequest extends AbstractModel {

    @JsonProperty("project")
    private ProjectData project;

    @Getter
    @AllArgsConstructor
    public static class ProjectData {

        @JsonProperty("name")
        @Size(min = 2, max = 100)
        private String name;

    }

}
