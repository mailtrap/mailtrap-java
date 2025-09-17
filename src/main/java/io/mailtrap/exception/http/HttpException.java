package io.mailtrap.exception.http;

import io.mailtrap.exception.BaseMailtrapException;
import lombok.Getter;

/**
 * Exception class representing an HTTP-related error.
 */
@Getter
public class HttpException extends BaseMailtrapException {

    /**
     * The status code of the HTTP response.
     */
    private final int statusCode;

    public HttpException(final String errorMessage, final int statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;
    }

}
