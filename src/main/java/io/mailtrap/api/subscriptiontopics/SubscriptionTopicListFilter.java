package io.mailtrap.api.subscriptiontopics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination parameters for listing subscription topics. All fields are optional;
 * {@code null} fields are omitted from the query string.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionTopicListFilter {

    /**
     * Number of topics per page (max 100, default 50).
     */
    private Integer perPage;

    /**
     * Page number to retrieve (page-token pagination, default 1).
     */
    private Integer token;
}
