package io.mailtrap.api.apiresource;

import io.mailtrap.MailtrapValidator;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;

public abstract class ApiResourceWithValidation extends ApiResource {

    /**
     * The custom validator used for validating email request bodies.
     */
    protected final MailtrapValidator mailtrapValidator;

    protected ApiResourceWithValidation(final MailtrapConfig config, final MailtrapValidator mailtrapValidator) {
        super(config);
        this.mailtrapValidator = mailtrapValidator;
    }

    protected <T> void validateRequestBodyAndThrowException(final T object) {
        if (object == null) {
            throw new InvalidRequestBodyException("Invalid request body. Violations: request must not be null");
        }

        final String violations = mailtrapValidator.validateAndGetViolationsAsString(object);

        if (!violations.isEmpty()) {
            throw new InvalidRequestBodyException("Invalid request body. Violations: " + violations);
        }
    }
}
