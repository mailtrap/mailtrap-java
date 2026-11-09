package io.mailtrap.model.request.accountaccesses;

import io.mailtrap.api.accountaccesses.AccountAccesses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListAccountAccessQueryParams {

    /**
     * The identifiers of the domains for which to include the results
     */
    // TODO update type to List<Long> as soon as domain UUIDs will use bigints as identifiers
    private List<String> domainUuids;

    /**
     * The identifiers of the sandboxes for which to include the results
     */
    private List<String> inboxIds;

    /**
     * The identifiers of the projects for which to include the results
     */
    private List<String> projectIds;

    /**
     * Creates an instance of {@link ListAccountAccessQueryParams} with no query parameters set.
     * This is useful when you want to call the {@link AccountAccesses#listUserAndInviteAccountAccesses} method without any query parameters.
     *
     * @return A new instance with no parameters set
     */
    public static ListAccountAccessQueryParams empty() {
        return new ListAccountAccessQueryParams();
    }
}
