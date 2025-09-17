package io.mailtrap.exception.http;

/**
 * Specific exception class representing a 4xx HTTP-related error.
 */
public class HttpClientException extends HttpException {
    public HttpClientException(final String errorMessage, final int statusCode) {
        super(errorMessage, statusCode);
    }
}
