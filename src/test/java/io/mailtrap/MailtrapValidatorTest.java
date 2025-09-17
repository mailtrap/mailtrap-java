package io.mailtrap;

import io.mailtrap.model.request.emails.Address;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MailtrapValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final MailtrapValidator mailtrapValidator = new MailtrapValidator(validator);

    @Test
    void validate_WithValidObject_ShouldReturnEmptyMap() {
        final Address validAddress = new Address("mail+123@gmail.com");

        Map<String, String> errors = mailtrapValidator.validate(validAddress);

        assertEquals(0, errors.size());
    }

    @Test
    void validate_WithInvalidObject_ShouldReturnErrorsMap() {
        final Address invalidAddress = new Address("qwerty");

        final Map<String, String> errors = mailtrapValidator.validate(invalidAddress);

        assertEquals(1, errors.size());
        assertEquals("must be a well-formed email address", errors.get("email"));
    }

    @Test
    void validateAndGetViolationsAsString_WithValidObject_ShouldReturnEmptyString() {
        final Address validAddress = new Address("mail+123@gmail.com");

        final String violationsAsString = mailtrapValidator.validateAndGetViolationsAsString(validAddress);

        assertEquals("", violationsAsString);
    }

    @Test
    void validateAndGetViolationsAsString_WithInvalidObject_ShouldReturnConcatenatedString() {
        final Address invalidAddress = new Address("qwerty");

        final String violationsAsString = mailtrapValidator.validateAndGetViolationsAsString(invalidAddress);

        assertEquals("email=must be a well-formed email address", violationsAsString);
    }

}
