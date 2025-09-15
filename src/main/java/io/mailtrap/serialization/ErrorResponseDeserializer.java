package io.mailtrap.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.mailtrap.model.response.ErrorResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Custom deserializer for {@link ErrorResponse} that handles the deserialization
 * of error messages from a variety of possible JSON structures.
 *
 * <p>This deserializer can handle different formats of error representations in JSON:
 * <ul>
 *   <li>An array of error messages under the "errors" field.</li>
 *   <li>A single textual error message under the "errors" or "error" field.</li>
 *   <li>An object containing nested fields with error messages under the "errors" field.</li>
 * </ul>
 *
 * <p>The deserializer collects all error messages into a list and constructs
 * an {@link ErrorResponse} object that encapsulates these messages.
 */
public class ErrorResponseDeserializer extends JsonDeserializer<ErrorResponse> {

    @Override
    public ErrorResponse deserialize(final JsonParser p, final DeserializationContext deserializationContext)
            throws IOException {
        final JsonNode node = p.getCodec().readTree(p);

        final List<String> errors = new ArrayList<>();

        if (node.has("errors")) {
            final JsonNode errorsNode = node.get("errors");
            if (errorsNode.isArray()) {
                final Iterator<JsonNode> elements = errorsNode.elements();
                while (elements.hasNext()) {
                    errors.add(elements.next().asText());
                }
            } else if (errorsNode.isTextual()) {
                errors.add(errorsNode.asText());
            } else if (errorsNode.isObject()) {
                final Iterator<Map.Entry<String, JsonNode>> fields = errorsNode.fields();
                while (fields.hasNext()) {
                    final Map.Entry<String, JsonNode> field = fields.next();
                    final JsonNode fieldValue = field.getValue();
                    if (fieldValue.isArray()) {
                        final Iterator<JsonNode> fieldElements = fieldValue.elements();
                        while (fieldElements.hasNext()) {
                            errors.add(fieldElements.next().asText());
                        }
                    } else if (fieldValue.isTextual()) {
                        errors.add(fieldValue.asText());
                    }
                }
            }
        } else if (node.has("error") && node.get("error").isTextual()) {
            errors.add(node.get("error").asText());
        }

        return new ErrorResponse(errors);
    }
}
