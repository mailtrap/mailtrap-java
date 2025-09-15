package io.mailtrap.api.messages;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;
import io.mailtrap.model.request.messages.ForwardMessageRequest;
import io.mailtrap.model.request.messages.UpdateMessageRequest;
import io.mailtrap.model.response.messages.*;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class MessagesImpl extends ApiResource implements Messages {

    public MessagesImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public MessageResponse getMessage(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d", accountId, inboxId, messageId),
            new RequestData(),
            MessageResponse.class
        );
    }

    @Override
    public MessageResponse updateMessage(final long accountId, final long inboxId, final long messageId, final UpdateMessageRequest request) {
        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d", accountId, inboxId, messageId),
            request,
            new RequestData(),
            MessageResponse.class
        );
    }

    @Override
    public MessageResponse deleteMessage(final long accountId, final long inboxId, final long messageId) {
        return httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d", accountId, inboxId, messageId),
            new RequestData(),
            MessageResponse.class
        );
    }

    @Override
    public List<MessageResponse> getMessages(final long accountId, final long inboxId, @NonNull final ListMessagesQueryParams listMessagesQueryParams) {
        final var queryParams = RequestData.buildQueryParams(
            entry("last_id", Optional.ofNullable(listMessagesQueryParams.getLastId())),
            entry("page", Optional.ofNullable(listMessagesQueryParams.getPage())),
            entry("search", Optional.ofNullable(listMessagesQueryParams.getSearch())));

        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages", accountId, inboxId),
            new RequestData(queryParams),
            MessageResponse.class
        );
    }

    @Override
    public ForwardMessageResponse forwardMessage(final long accountId, final long inboxId, final long messageId, final ForwardMessageRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/forward", accountId, inboxId, messageId),
            request,
            new RequestData(),
            ForwardMessageResponse.class
        );
    }

    @Override
    public MessageSpamScoreResponse getSpamScore(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/spam_report", accountId, inboxId, messageId),
            new RequestData(),
            MessageSpamScoreResponse.class
        );
    }

    @Override
    public MessageHtmlAnalysisResponse getMessageHtmlAnalysis(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/analyze", accountId, inboxId, messageId),
            new RequestData(),
            MessageHtmlAnalysisResponse.class
        );
    }

    @Override
    public String getTextMessage(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/body.txt", accountId, inboxId, messageId),
            new RequestData(),
            String.class
        );
    }

    @Override
    public String getRawMessage(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/body.raw", accountId, inboxId, messageId),
            new RequestData(),
            String.class
        );
    }

    @Override
    public String getMessageSource(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/body.htmlsource", accountId, inboxId, messageId),
            new RequestData(),
            String.class
        );
    }

    @Override
    public String getHtmlMessage(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/body.html", accountId, inboxId, messageId),
            new RequestData(),
            String.class
        );
    }

    @Override
    public String getMessageAsEml(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/body.eml", accountId, inboxId, messageId),
            new RequestData(),
            String.class
        );
    }

    @Override
    public MessageHeadersResponse getMailHeaders(final long accountId, final long inboxId, final long messageId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/inboxes/%d/messages/%d/mail_headers", accountId, inboxId, messageId),
            new RequestData(),
            MessageHeadersResponse.class
        );
    }
}
