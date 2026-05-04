package io.mailtrap.api.apitokens;

import io.mailtrap.model.request.apitokens.CreateApiTokenRequest;
import io.mailtrap.model.response.apitokens.ApiToken;
import io.mailtrap.model.response.apitokens.ApiTokenWithToken;

import java.util.List;

public interface ApiTokens {

    /**
     * List all API tokens visible to the current API token.
     *
     * @param accountId unique account ID
     * @return list of API tokens
     */
    List<ApiToken> getAllApiTokens(long accountId);

    /**
     * Create a new API token for the account with the given name and resource permissions.
     * The full token value is returned only on creation.
     *
     * @param accountId unique account ID
     * @param request   token name and resource permissions
     * @return created token, including the full token value
     */
    ApiTokenWithToken createApiToken(long accountId, CreateApiTokenRequest request);

    /**
     * Get a single API token by id.
     *
     * @param accountId unique account ID
     * @param id        API token ID
     * @return API token
     */
    ApiToken getApiToken(long accountId, long id);

    /**
     * Permanently delete an API token.
     *
     * @param accountId unique account ID
     * @param id        API token ID
     */
    void deleteApiToken(long accountId, long id);

    /**
     * Reset an API token. Expires the requested token and creates a new one with the same
     * permissions; the new token value is returned only once.
     *
     * @param accountId unique account ID
     * @param id        API token ID
     * @return new token, including the full token value
     */
    ApiTokenWithToken resetApiToken(long accountId, long id);

}
