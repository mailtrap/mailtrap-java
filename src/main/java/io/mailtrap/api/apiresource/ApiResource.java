package io.mailtrap.api.apiresource;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.CustomHttpClient;
import io.mailtrap.http.impl.DefaultMailtrapHttpClient;

/**
 * Base abstract class representing an API resource.
 */
public abstract class ApiResource {

    /**
     * HTTP client for making API requests.
     */
    protected final CustomHttpClient httpClient;
    /**
     * The authentication token used for API requests.
     */
    protected final String token;
    /**
     * The API host
     */
    protected String apiHost;

    protected ApiResource(final MailtrapConfig config) {
        this.httpClient = config.getHttpClient() != null
            ? config.getHttpClient()
            : new DefaultMailtrapHttpClient(config);
        this.token = config.getToken();
    }

}
