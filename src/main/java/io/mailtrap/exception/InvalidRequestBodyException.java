package io.mailtrap.exception;

/**
 * Custom exception wrapper for request body validation violations
 */
public class InvalidRequestBodyException extends BaseMailtrapException {

    public InvalidRequestBodyException(final String errorMessage) {
        super(errorMessage);
    }

    public InvalidRequestBodyException(final String errorMessage, final Exception cause) {
        super(errorMessage, cause);
    }

}
