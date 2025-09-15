package io.mailtrap.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mailtrap.Mapper;
import io.mailtrap.model.response.messages.BlacklistReportInfoResult;
import io.mailtrap.model.response.messages.BlacklistsReportInfo;
import io.mailtrap.model.response.messages.MessageResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlacklistsReportInfoDeserializerTest {

    private final ObjectMapper mapper = Mapper.get();

    @Test
    void testApiTokenSpecifierDeserialization() throws Exception {
        final String json = """
                    {
                      "id": 3,
                      "inbox_id": 2,
                      "blacklists_report_info": false
                    }
                """;

        final MessageResponse message = mapper.readValue(json, MessageResponse.class);

        assertEquals(3, message.getId());
        assertEquals(2, message.getInboxId());
        assertNotNull(message.getBlacklistsReportInfoWrapper());
        assertTrue(message.getBlacklistsReportInfoWrapper().isBoolean());
        assertFalse(message.getBlacklistsReportInfoWrapper().getBooleanValue());
    }

    @Test
    void testApiTokenSpecifierDeserialization2() throws Exception {
        final String json = """
                    {
                      "id": 4,
                      "inbox_id": 5,
                      "blacklists_report_info": {
                        "result": "success",
                        "domain": "mailtrap.io",
                        "ip": "192.168.1.10",
                        "report": [
                          {
                            "name": "SpamCheck",
                            "url": "https://blacklist.example.com/report/spamcheck",
                            "in_black_list": false
                          }
                        ]
                      }
                    }
                """;

        final MessageResponse message = mapper.readValue(json, MessageResponse.class);

        assertEquals(4, message.getId());
        assertEquals(5, message.getInboxId());
        assertNotNull(message.getBlacklistsReportInfoWrapper());
        assertFalse(message.getBlacklistsReportInfoWrapper().isBoolean());

        final BlacklistsReportInfo blacklistsReportInfo = message.getBlacklistsReportInfoWrapper().getObjectValue();
        assertEquals(BlacklistReportInfoResult.SUCCESS, blacklistsReportInfo.getResult());
        assertEquals(1, blacklistsReportInfo.getReport().size());
    }

}
