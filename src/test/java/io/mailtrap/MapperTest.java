package io.mailtrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mailtrap.model.AccessLevel;
import io.mailtrap.model.WebhookEventType;
import io.mailtrap.model.response.accounts.AccountsResponse;
import io.mailtrap.model.response.suppressions.SuppressionType;
import io.mailtrap.model.response.suppressions.SuppressionsResponse;
import io.mailtrap.model.response.webhooks.Webhook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapperTest {

    @Test
    void unknownEnumValue_readsAsNull() throws JsonProcessingException {
        final SuppressionsResponse suppression = Mapper.get().readValue(
            "{\"id\": \"1\", \"type\": \"a type added later\", \"email\": \"email@mailtrap.io\"}", SuppressionsResponse.class);

        assertNull(suppression.getType());
        assertEquals("email@mailtrap.io", suppression.getEmail());
    }

    @Test
    void unknownEnumValueInAList_isLeftOut() throws JsonProcessingException {
        final Webhook webhook = Mapper.get().readValue(
            "{\"id\": 1, \"event_types\": [\"delivery\", \"an_event_type_added_later\", \"subscription_preferences_update\"]}",
            Webhook.class);

        assertEquals(List.of(WebhookEventType.DELIVERY, WebhookEventType.SUBSCRIPTION_PREFERENCES_UPDATE), webhook.getEventTypes());
    }

    @Test
    void unknownAccessLevel_isLeftOut() throws JsonProcessingException {
        final AccountsResponse account = Mapper.get().readValue(
            "{\"id\": 1, \"name\": \"Account\", \"access_levels\": [1000, 7, \"a level added later\"]}", AccountsResponse.class);

        assertEquals(List.of(AccessLevel.OWNER), account.getAccessLevels());
    }

    @Test
    void fromValue_stillThrowsForAnUnknownValue() {
        assertThrows(IllegalArgumentException.class, () -> SuppressionType.fromValue("a type added later"));
    }
}
