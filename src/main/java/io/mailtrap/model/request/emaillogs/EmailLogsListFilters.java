package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filters for listing email logs. All fields are optional.
 * Date range uses sent_after and sent_before (ISO 8601 date-time strings).
 * Other filters use concrete types so operators are enforced per field.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLogsListFilters {

    private String sentAfter;
    private String sentBefore;
    private FilterCiString to;
    private FilterCiString from;
    private FilterOptionalString subject;
    private FilterStatus status;
    private FilterEvents events;
    private FilterNumber clicksCount;
    private FilterNumber opensCount;
    private FilterString clientIp;
    private FilterString sendingIp;
    private FilterCiString emailServiceProviderResponse;
    private FilterExactString emailServiceProvider;
    private FilterExactString recipientMx;
    private FilterExactString category;
    private FilterSendingDomainId sendingDomainId;
    private FilterSendingStream sendingStream;
}
