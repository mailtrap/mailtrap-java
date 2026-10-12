package io.mailtrap.api.inbound;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.inbound.InboundThread;
import io.mailtrap.model.response.inbound.InboundThreadsListResponse;

import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class InboundThreadsImpl extends ApiResource implements InboundThreads {

    public InboundThreadsImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public InboundThreadsListResponse list(final long inboxId, final String lastId) {
        return list(inboxId, lastId, null);
    }

    @Override
    public InboundThreadsListResponse list(final long inboxId, final String lastId, final String search) {
        final var queryParams = RequestData.buildQueryParams(
            entry("last_id", Optional.ofNullable(lastId)),
            entry("search", Optional.ofNullable(search))
        );
        return httpClient.get(
            String.format(apiHost + "/api/inbound/inboxes/%d/threads", inboxId),
            new RequestData(queryParams),
            InboundThreadsListResponse.class
        );
    }

    @Override
    public InboundThread get(final long inboxId, final String threadId) {
        return httpClient.get(
            String.format(apiHost + "/api/inbound/inboxes/%d/threads/%s", inboxId, threadId),
            new RequestData(),
            InboundThread.class
        );
    }

    @Override
    public void delete(final long inboxId, final String threadId) {
        httpClient.delete(
            String.format(apiHost + "/api/inbound/inboxes/%d/threads/%s", inboxId, threadId),
            new RequestData(),
            Void.class
        );
    }
}
