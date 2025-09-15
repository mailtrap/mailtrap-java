package io.mailtrap.api.suppressions;

import io.mailtrap.model.response.suppressions.SuppressionsResponse;

import java.util.List;

public interface Suppressions {

    /**
     * List and search suppressions by email. The endpoint returns up to 1000 suppressions per request.
     *
     * @param accountId - unique account ID
     * @param email     - search suppressions for this email
     * @return a list of suppressions
     */
    List<SuppressionsResponse> search(long accountId, String email);

    /**
     * Delete a suppression by ID. Mailtrap will no longer prevent sending to this email unless it's recorded in suppressions again.
     *
     * @param accountId     - unique account ID
     * @param suppressionId - unique suppression ID
     * @return the attributes of the deleted suppression
     */
    SuppressionsResponse deleteSuppression(long accountId, String suppressionId);

}
