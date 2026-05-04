package io.mailtrap.api.apitokens;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.AbstractModel;
import io.mailtrap.model.request.apitokens.CreateApiTokenRequest;
import io.mailtrap.model.response.apitokens.ApiToken;
import io.mailtrap.model.response.apitokens.ApiTokenWithToken;

import java.util.List;

public class ApiTokensImpl extends ApiResource implements ApiTokens {

    public ApiTokensImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public List<ApiToken> getAllApiTokens(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/api_tokens", accountId),
            new RequestData(),
            ApiToken.class
        );
    }

    @Override
    public ApiTokenWithToken createApiToken(final long accountId, final CreateApiTokenRequest request) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/api_tokens", accountId),
            request,
            new RequestData(),
            ApiTokenWithToken.class
        );
    }

    @Override
    public ApiToken getApiToken(final long accountId, final long id) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/api_tokens/%d", accountId, id),
            new RequestData(),
            ApiToken.class
        );
    }

    @Override
    public void deleteApiToken(final long accountId, final long id) {
        httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/api_tokens/%d", accountId, id),
            new RequestData(),
            Void.class
        );
    }

    @Override
    public ApiTokenWithToken resetApiToken(final long accountId, final long id) {
        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/api_tokens/%d/reset", accountId, id),
            (AbstractModel) null,
            new RequestData(),
            ApiTokenWithToken.class
        );
    }
}
