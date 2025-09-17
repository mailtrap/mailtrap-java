package io.mailtrap.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mailtrap.model.response.messages.BlacklistsReportInfo;
import io.mailtrap.model.response.messages.BlacklistsReportInfoWrapper;

import java.io.IOException;

public class BlacklistsReportInfoDeserializer extends JsonDeserializer<BlacklistsReportInfoWrapper> {

    @Override
    public BlacklistsReportInfoWrapper deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.isBoolean()) {
            return new BlacklistsReportInfoWrapper(node.asBoolean());
        } else {
            final ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            return new BlacklistsReportInfoWrapper(mapper.convertValue(node, BlacklistsReportInfo.class));
        }
    }

}
