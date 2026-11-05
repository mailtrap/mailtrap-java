package io.mailtrap.api.trackingoptouts;

import io.mailtrap.model.request.trackingoptouts.CreateTrackingOptOutRequest;
import io.mailtrap.model.request.trackingoptouts.TrackingOptOutListFilter;
import io.mailtrap.model.response.trackingoptouts.TrackingOptOut;
import io.mailtrap.model.response.trackingoptouts.TrackingOptOutListResponse;

public interface TrackingOptOuts {

    /**
     * List email addresses that have opted out of open and click tracking.
     * The endpoint returns up to 1000 records per request; pass the previous response's
     * {@code lastId} to fetch the next page.
     *
     * @param filter - optional filtering and pagination parameters
     * @return the page of tracking opt-outs and the cursor for the next page
     */
    TrackingOptOutListResponse getTrackingOptOuts(TrackingOptOutListFilter filter);

    /**
     * Add an email address to the tracking opt-out list for a domain.
     *
     * @param request - request data
     * @return the created tracking opt-out
     */
    TrackingOptOut createTrackingOptOut(CreateTrackingOptOutRequest request);

    /**
     * Remove an email address from the tracking opt-out list so open and click tracking can
     * apply again.
     *
     * @param trackingOptOutId - unique tracking opt-out ID
     * @return the attributes of the deleted tracking opt-out
     */
    TrackingOptOut deleteTrackingOptOut(String trackingOptOutId);

}
