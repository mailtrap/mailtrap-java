package io.mailtrap;

import jakarta.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper for validating objects using a javax.validation.Validator.
 */
public class MailtrapValidator {
    private final Validator validator;

    public MailtrapValidator(final Validator validator) {
        this.validator = validator;
    }

    /**
     * Validates the given object and returns a map of validation errors.
     *
     * <p>The map contains property names as keys and corresponding error messages as values.
     * If the object has no validation errors, the map will be empty.
     *
     * @param <T>              the type of the object to validate
     * @param objectToValidate the object to validate
     * @return a map of validation errors, with property names as keys and error messages as values
     */
    public <T> Map<String, String> validate(final T objectToValidate) {
        final var violations = validator.validate(objectToValidate);

        final var errors = new HashMap<String, String>();

        for (final var violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return errors;
    }

    /**
     * Validates the given object and returns a string representation of the validation errors.
     *
     * <p>The returned string contains each validation error in the format "propertyName=errorMessage",
     * with errors separated by semicolons. If there are no validation errors, an empty string is returned.
     *
     * @param <T>              the type of the object to validate
     * @param objectToValidate the object to validate
     * @return a string representation of the validation errors, or an empty string if there are no errors
     */
    public <T> String validateAndGetViolationsAsString(final T objectToValidate) {
        return this.validate(objectToValidate).entrySet().stream()
                .map(violation -> violation.getKey() + "=" + violation.getValue())
                .collect(Collectors.joining("; "));
    }
}
