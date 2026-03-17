package io.mailtrap.http;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.exception.http.HttpException;
import io.mailtrap.exception.http.HttpServerException;
import io.mailtrap.http.impl.DefaultMailtrapHttpClient;
import io.mailtrap.model.AbstractModel;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface representing a custom HTTP client for accessing API.
 * Default implementation {@link DefaultMailtrapHttpClient} uses Java's {@link HttpClient}
 * <p>
 * Implement your own client to use different http client or handle API communications in a different manner and pass it via {@link MailtrapConfig}
 */
public interface CustomHttpClient {

    /**
     * @param url          Request url
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T> T get(String url, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * @param url          Request url
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T> List<T> getList(String url, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * @param url          Request url
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T> T delete(String url, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * @param url          Request url
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T> T head(String url, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * @param url          Request url
     * @param data         Request body. By default, would be converted to string using {@link AbstractModel#toJson()}
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @param <V>          Request type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T, V extends AbstractModel> T post(String url, V data, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * @param url          Request url
     * @param data         Request body. By default, would be converted to string using {@link AbstractModel#toJson()}
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @param <V>          Request type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T, V extends AbstractModel> T put(String url, V data, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * @param url          Request url
     * @param data         Request body. By default, would be converted to string using {@link AbstractModel#toJson()}
     * @param requestData  Additional request data - headers and query parameters
     * @param responseType Return class type
     * @param <T>          Return type
     * @param <V>          Request type
     * @return Response type
     * @throws HttpException in case any error. Might throw specific {@link HttpClientException} for HTTP response codes 4xx or {@link HttpServerException} for HTTP response codes 5xx
     */
    <T, V extends AbstractModel> T patch(String url, V data, RequestData requestData, Class<T> responseType) throws HttpException;

    /**
     * Appends query parameters to the given URL.
     *
     * @param url       The base URL
     * @param urlParams A map containing query parameters to append to the URL.
     *                  The keys represent parameter names, and the values represent optional parameter values.
     *                  If a parameter value is absent, it will be skipped.
     * @return The URL with appended query parameters. If no parameters are provided, returns the original URL unchanged.
     */
    default String appendUrlParams(String url, Map<String, ? extends Optional<?>> urlParams) {
        if (urlParams.isEmpty()
                || urlParams.entrySet()
                        .stream()
                        .noneMatch(e -> e.getValue().isPresent())) {
            return url;
        }

        return url + urlParams.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .flatMap(entry -> {
                    Object value = entry.getValue().get();
                    if (value instanceof Collection<?> collection) {
                        return collection.stream()
                                .filter(Objects::nonNull)
                                .filter(v -> !v.toString().isBlank())
                                .map(v -> entry.getKey() + "=" + URLEncoder.encode(v.toString(), StandardCharsets.UTF_8));
                    } else {
                        return Stream.of(entry.getKey() + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
                    }
                })
                .collect(Collectors.joining("&", "?", ""));
    }
}
