package io.mailtrap.exception;

/**
 * Custom exception wrapper for request body validation violations
 */
public class InvalidRequestBodyException extends BaseMailtrapException {

    public InvalidRequestBodyException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidRequestBodyException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
    }

}
