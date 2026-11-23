package io.mailtrap.model.response.subscriptiontopics;

import io.mailtrap.model.response.Pagination;
import lombok.Data;

import java.util.List;

/**
 * Paginated list of subscription topics, wrapped as {@code data} alongside page-token
 * pagination metadata.
 */
@Data
public class SubscriptionTopicListResponse {

    private List<SubscriptionTopic> data;

    private Pagination pagination;

}
