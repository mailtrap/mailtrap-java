package io.mailtrap.http.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.mailtrap.Mapper;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.BaseMailtrapException;
import io.mailtrap.exception.JsonException;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.exception.http.HttpException;
import io.mailtrap.exception.http.HttpServerException;
import io.mailtrap.http.CustomHttpClient;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.AbstractModel;
import io.mailtrap.model.response.ErrorResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultMailtrapHttpClient implements CustomHttpClient {

    private final String token;
    private final HttpClient httpClient;

    public DefaultMailtrapHttpClient(final MailtrapConfig config) {
        this.token = config.getToken();

        final HttpClient.Builder httpClientBuilder = HttpClient.newBuilder();
        if (config.getConnectionTimeout() != null) {
            httpClientBuilder.connectTimeout(config.getConnectionTimeout());
        }

        this.httpClient = httpClientBuilder.build();
    }

    @Override
    public <T> T get(final String url, final RequestData requestData, final Class<T> responseType) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .GET()
                .build();

        return this.request(httpRequest, responseType);
    }

    @Override
    public <T> List<T> getList(final String url, final RequestData requestData, final Class<T> responseClass) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .GET()
                .build();

        final JavaType responseType = TypeFactory.defaultInstance().constructCollectionType(List.class, responseClass);

        return this.request(httpRequest, responseType);
    }

    @Override
    public <T> T delete(final String url, final RequestData requestData, final Class<T> responseType) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .DELETE()
                .build();

        return this.request(httpRequest, responseType);
    }

    @Override
    public <T> T head(final String url, final RequestData requestData, final Class<T> responseType) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        return this.request(httpRequest, responseType);
    }

    @Override
    public <T, V extends AbstractModel> T post(final String url, final V data, final RequestData requestData, final Class<T> responseType) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .POST(getBodyPublisher(data))
                .build();

        return this.request(httpRequest, responseType);
    }

    @Override
    public <T, V extends AbstractModel> T put(final String url, final V data, final RequestData requestData, final Class<T> responseType) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .PUT(getBodyPublisher(data))
                .build();

        return this.request(httpRequest, responseType);
    }

    @Override
    public <T, V extends AbstractModel> T patch(final String url, final V data, final RequestData requestData, final Class<T> responseType) throws HttpException {
        final HttpRequest httpRequest = prepareRequest(url, requestData)
                .method("PATCH", getBodyPublisher(data))
                .build();

        return this.request(httpRequest, responseType);
    }

    private <T> T request(final HttpRequest request, final Class<T> responseType) throws HttpException {
        try {
            final var send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return handleResponse(send, responseType);
        } catch (final InterruptedException | IOException e) {
            throw new BaseMailtrapException("An error has occurred while sending request", e);
        }
    }

    private <T> T request(final HttpRequest request, final JavaType responseType) throws HttpException {
        try {
            final var send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return handleResponse(send, responseType);
        } catch (final InterruptedException | IOException e) {
            throw new BaseMailtrapException("An error has occurred while sending request", e);
        }
    }

    private <T> T handleResponse(final HttpResponse<String> response, final Class<T> responseType) throws HttpException {
        return handleResponseInternal(response, responseType, null);
    }

    private <T> T handleResponse(final HttpResponse<String> response, final JavaType responseType) throws HttpException {
        return handleResponseInternal(response, null, responseType);
    }

    private <T> T handleResponseInternal(final HttpResponse<String> response, final Class<T> responseClassType, final JavaType responseJavaType) throws HttpException {
        try {
            if (response.body() == null) {
                throw new BaseMailtrapException("Response body is null");
            }

            final int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode < 300) {
                if (responseClassType != null) {
                    if (String.class.equals(responseClassType)) {
                        return responseClassType.cast(response.body());
                    }
                    return Mapper.get().readValue(response.body(), responseClassType);
                } else if (responseJavaType != null) {
                    return Mapper.get().readValue(response.body(), responseJavaType);
                } else {
                    throw new IllegalArgumentException("Both responseType and typeReference are null");
                }
            } else if (statusCode >= 400 && statusCode < 500) {
                final ErrorResponse errorResponse = Mapper.get().readValue(response.body(), ErrorResponse.class);
                throw new HttpClientException(String.join(", ", errorResponse.getErrors()), statusCode);
            } else if (statusCode >= 500) {
                throw new HttpServerException(String.format("Internal Server Error. HTTP response code (%d) received from the API server. Retry later or contact support.", statusCode), statusCode);
            }
            throw new HttpException(String.format("HTTP response code (%d) received from the API server (no error info)", statusCode), statusCode);
        } catch (final IOException e) {
            throw new JsonException("An error has occurred while converting JSON", e);
        }
    }

    private HttpRequest.Builder prepareRequest(final String url, final RequestData requestData) {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(this.appendUrlParams(url, requestData.getQueryParams())))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .header("User-Agent", "mailtrap-java (https://github.com/mailtrap/mailtrap-java)");

        final Map<String, Object> headers = new HashMap<>(requestData.getHeaders());
        for (Map.Entry<String, ?> entry : headers.entrySet()) {
            requestBuilder = requestBuilder.header(entry.getKey(), entry.getValue().toString());
        }

        return requestBuilder;
    }

    private static <V extends AbstractModel> HttpRequest.BodyPublisher getBodyPublisher(final V data) {
        return data == null ?
                HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(data.toJson());
    }
}
