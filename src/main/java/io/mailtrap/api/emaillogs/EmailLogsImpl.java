package io.mailtrap.api.emaillogs;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.emaillogs.EmailLogFilter;
import io.mailtrap.model.request.emaillogs.EmailLogsListFilters;
import io.mailtrap.model.response.emaillogs.EmailLogsListResponse;
import io.mailtrap.model.response.emaillogs.EmailLogMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EmailLogsImpl extends ApiResource implements EmailLogs {

    public EmailLogsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public EmailLogsListResponse list(final long accountId, final String searchAfter,
            final EmailLogsListFilters filters) {
        final String queryString = buildQueryString(searchAfter, filters);
        final String url = String.format("%s/api/accounts/%d/email_logs", apiHost, accountId)
                + (queryString.isEmpty() ? "" : "?" + queryString);

        return httpClient.get(url, new RequestData(), EmailLogsListResponse.class);
    }

    @Override
    public EmailLogMessage get(final long accountId, final String sendingMessageId) {
        if (sendingMessageId == null || sendingMessageId.isBlank()) {
            throw new IllegalArgumentException("sendingMessageId must not be null or blank");
        }
        final String url = String.format("%s/api/accounts/%d/email_logs/%s", apiHost, accountId, sendingMessageId);
        return httpClient.get(url, new RequestData(), EmailLogMessage.class);
    }

    private static String buildQueryString(final String searchAfter, final EmailLogsListFilters filters) {
        final List<String> params = new ArrayList<>();

        if (searchAfter != null && !searchAfter.isBlank()) {
            params.add(enc("search_after") + "=" + enc(searchAfter));
        }

        if (filters != null) {
            appendFilter(params, "sent_after", filters.getSentAfter());
            appendFilter(params, "sent_before", filters.getSentBefore());
            appendOperatorValue(params, "to", filters.getTo());
            appendOperatorValue(params, "from", filters.getFrom());
            appendOperatorValue(params, "subject", filters.getSubject());
            appendOperatorValue(params, "status", filters.getStatus());
            appendOperatorValue(params, "events", filters.getEvents());
            appendOperatorValue(params, "clicks_count", filters.getClicksCount());
            appendOperatorValue(params, "opens_count", filters.getOpensCount());
            appendOperatorValue(params, "client_ip", filters.getClientIp());
            appendOperatorValue(params, "sending_ip", filters.getSendingIp());
            appendOperatorValue(params, "email_service_provider_response", filters.getEmailServiceProviderResponse());
            appendOperatorValue(params, "email_service_provider", filters.getEmailServiceProvider());
            appendOperatorValue(params, "recipient_mx", filters.getRecipientMx());
            appendOperatorValue(params, "category", filters.getCategory());
            appendOperatorValue(params, "sending_domain_id", filters.getSendingDomainId());
            appendOperatorValue(params, "sending_stream", filters.getSendingStream());
        }

        return String.join("&", params);
    }

    private static void appendFilter(final List<String> params, final String key, final String value) {
        if (value != null && !value.isBlank()) {
            params.add(enc("filters[" + key + "]") + "=" + enc(value));
        }
    }

    private static void appendOperatorValue(final List<String> params, final String field, final EmailLogFilter spec) {
        if (spec == null)
            return;
        final String operator = spec.getOperatorString();
        if (operator == null || operator.isBlank())
            return;
        params.add(enc("filters[" + field + "][operator]") + "=" + enc(operator));
        final Object value = spec.getValue();
        if (value != null) {
            final List<String> values = toValueList(value);
            final String valueKey = values.size() > 1
                    ? "filters[" + field + "][value][]"
                    : "filters[" + field + "][value]";
            for (final String v : values) {
                params.add(enc(valueKey) + "=" + enc(String.valueOf(v)));
            }
        }
    }

    private static List<String> toValueList(final Object value) {
        if (value instanceof Collection<?> c) {
            return c.stream()
                    .filter(v -> v != null)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(String.valueOf(value));
    }

    private static String enc(final String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
