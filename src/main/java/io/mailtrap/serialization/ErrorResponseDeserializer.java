package io.mailtrap.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.mailtrap.model.response.ErrorResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for {@link ErrorResponse} that collects the error messages of an error body: every
 * string at any depth under its "errors" field (a string, a list, an object of fields, or a list of such
 * objects, as contact imports return) or, without one, under its "error" field.
 */
public class ErrorResponseDeserializer extends JsonDeserializer<ErrorResponse> {

    @Override
    public ErrorResponse deserialize(final JsonParser p, final DeserializationContext deserializationContext)
            throws IOException {
        final JsonNode node = p.getCodec().readTree(p);

        final List<String> errors = new ArrayList<>();
        collectMessages(node.has("errors") ? node.get("errors") : node.path("error"), errors);

        return new ErrorResponse(errors);
    }

    private static void collectMessages(final JsonNode node, final List<String> errors) {
        if (node.isTextual()) {
            errors.add(node.asText());
        } else if (node.isContainerNode()) {
            node.elements().forEachRemaining(element -> collectMessages(element, errors));
        }
    }
}
