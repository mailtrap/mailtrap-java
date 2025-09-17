package io.mailtrap.exception.http;

/**
 * Specific exception class representing a 5xx HTTP-related error.
 */
public class HttpServerException extends HttpException {
    public HttpServerException(final String errorMessage, final int statusCode) {
        super(errorMessage, statusCode);
    }
}
