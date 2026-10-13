package io.mailtrap.api.marketingstats;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.marketingstats.MarketingStatsResponse;

import java.util.Map;
import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class MarketingStatsImpl extends ApiResource implements MarketingStats {

    private static final String BASE_PATH = "/api/marketing/stats";

    public MarketingStatsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public MarketingStatsResponse getMarketingStats(final MarketingStatsFilter filter) {
        return httpClient.get(
            apiHost + BASE_PATH,
            new RequestData(buildQueryParams(filter)),
            MarketingStatsResponse.class
        );
    }

    private Map<String, ? extends Optional<?>> buildQueryParams(final MarketingStatsFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("filter must not be null");
        }
        if (filter.getStartDate() == null || filter.getStartDate().isBlank()) {
            throw new IllegalArgumentException("startDate must not be null or empty");
        }
        if (filter.getEndDate() == null || filter.getEndDate().isBlank()) {
            throw new IllegalArgumentException("endDate must not be null or empty");
        }

        return RequestData.buildQueryParams(
            entry("start_date", Optional.of(filter.getStartDate())),
            entry("end_date", Optional.of(filter.getEndDate())),
            entry("sending_domain_ids[]", Optional.ofNullable(filter.getSendingDomainIds())),
            entry("sources[]", Optional.ofNullable(filter.getSources())),
            entry("email_service_providers[]", Optional.ofNullable(filter.getEmailServiceProviders()))
        );
    }
}
