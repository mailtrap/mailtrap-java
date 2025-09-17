package io.mailtrap.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import io.mailtrap.Mapper;
import io.mailtrap.model.response.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseDeserializerTest {

    private final ErrorResponseDeserializer deserializer = new ErrorResponseDeserializer();

    @Test
    void deserialize_JsonWithTwoErrors_ShouldReturnListWithTwoEntries() throws Exception {
        final var errorJson = """
                {"errors": ["Unauthorized", "Your token is not valid"]}""";
        try (final JsonParser parser = new JsonFactory().createParser(errorJson)) {
            parser.setCodec(Mapper.get());
            final ErrorResponse deserialize = deserializer.deserialize(parser, null);

            assertEquals(2, deserialize.getErrors().size());
        }
    }

    @Test
    void deserialize_JsonWithSingleError_ShouldReturnListWithOneEntry() throws Exception {
        final var errorJson = """
                {"error": "Unauthorized"}""";
        try (final JsonParser parser = new JsonFactory().createParser(errorJson)) {
            parser.setCodec(Mapper.get());
            final ErrorResponse deserialize = deserializer.deserialize(parser, null);

            assertEquals(1, deserialize.getErrors().size());
        }
    }

    @Test
    void deserialize_ErrorsIsAnObject_ShouldReturnListWithOneEntry() throws Exception {
        final var errorJson = """
                {"errors":{"name":["You've reached the projects limit. Please upgrade your plan to create the new project."]}}
                """;
        try (final JsonParser parser = new JsonFactory().createParser(errorJson)) {
            parser.setCodec(Mapper.get());
            final ErrorResponse deserialize = deserializer.deserialize(parser, null);

            assertEquals(1, deserialize.getErrors().size());
        }
    }
}
