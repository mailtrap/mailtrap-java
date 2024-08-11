package io.mailtrap.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mailtrap.model.AbstractModel;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateInboxRequest extends AbstractModel {

    @JsonProperty("inbox")
    private InboxUpdateData inboxUpdateData;

    @Getter
    @AllArgsConstructor
    public static class InboxUpdateData {

        @JsonProperty("name")
        @Size(min = 2, max = 100)
        private String name;

        @JsonProperty("email_username")
        private String emailUsername;

    }

}
