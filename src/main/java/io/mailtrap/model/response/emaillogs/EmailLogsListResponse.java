package io.mailtrap.model.response.emaillogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmailLogsListResponse {

    @JsonProperty("messages")
    private List<EmailLogMessageSummary> messages;

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("next_page_cursor")
    private String nextPageCursor;
}
