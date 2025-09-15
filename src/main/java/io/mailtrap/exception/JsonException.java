package io.mailtrap.exception;

/**
 * Custom exception wrapper for JSON serializing/deserializing exceptions
 */
public class JsonException extends BaseMailtrapException {

    public JsonException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

}
