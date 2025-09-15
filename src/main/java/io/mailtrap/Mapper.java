package io.mailtrap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mailtrap.model.response.ErrorResponse;
import io.mailtrap.serialization.ErrorResponseDeserializer;

/**
 * Utility class for providing a configured ObjectMapper instance.
 */
public class Mapper {
    private static final ObjectMapper mapper = JsonMapper.builder()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .serializationInclusion(JsonInclude.Include.NON_EMPTY)
            .addModule(new SimpleModule().addDeserializer(ErrorResponse.class, new ErrorResponseDeserializer()))
            .addModule(new JavaTimeModule())
            .build();

    public static ObjectMapper get() {
        return mapper;
    }
}
