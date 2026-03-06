package io.mailtrap.api.stats;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.stats.SendingStatGroupResponse;
import io.mailtrap.model.response.stats.SendingStatsResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class StatsImpl extends ApiResource implements Stats {

    public StatsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public SendingStatsResponse getStats(final long accountId, final StatsFilter filter) {
        return httpClient.get(
            buildUrl(accountId, ""),
            new RequestData(buildStatsQueryParams(filter)),
            SendingStatsResponse.class
        );
    }

    @Override
    public List<SendingStatGroupResponse> byDomains(final long accountId, final StatsFilter filter) {
        return httpClient.getList(
            buildUrl(accountId, "/domains"),
            new RequestData(buildStatsQueryParams(filter)),
            SendingStatGroupResponse.class
        );
    }

    @Override
    public List<SendingStatGroupResponse> byCategories(final long accountId, final StatsFilter filter) {
        return httpClient.getList(
            buildUrl(accountId, "/categories"),
            new RequestData(buildStatsQueryParams(filter)),
            SendingStatGroupResponse.class
        );
    }

    @Override
    public List<SendingStatGroupResponse> byEmailServiceProviders(final long accountId, final StatsFilter filter) {
        return httpClient.getList(
            buildUrl(accountId, "/email_service_providers"),
            new RequestData(buildStatsQueryParams(filter)),
            SendingStatGroupResponse.class
        );
    }

    @Override
    public List<SendingStatGroupResponse> byDate(final long accountId, final StatsFilter filter) {
        return httpClient.getList(
            buildUrl(accountId, "/date"),
            new RequestData(buildStatsQueryParams(filter)),
            SendingStatGroupResponse.class
        );
    }

    private String buildUrl(final long accountId, final String suffix) {
        return String.format(apiHost + "/api/accounts/%d/stats%s", accountId, suffix);
    }

    private Map<String, ? extends Optional<?>> buildStatsQueryParams(final StatsFilter filter) {
        return RequestData.buildQueryParams(
            entry("start_date", Optional.ofNullable(filter.getStartDate())),
            entry("end_date", Optional.ofNullable(filter.getEndDate())),
            entry("sending_domain_ids[]", Optional.ofNullable(filter.getSendingDomainIds())),
            entry("sending_streams[]", Optional.ofNullable(filter.getSendingStreams())),
            entry("categories[]", Optional.ofNullable(filter.getCategories())),
            entry("email_service_providers[]", Optional.ofNullable(filter.getEmailServiceProviders()))
        );
    }
}
