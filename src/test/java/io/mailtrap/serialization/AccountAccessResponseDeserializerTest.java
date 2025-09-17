package io.mailtrap.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mailtrap.Mapper;
import io.mailtrap.model.response.accountaccesses.AccountAccessResponse;
import io.mailtrap.model.response.accountaccesses.ApiTokenSpecifier;
import io.mailtrap.model.response.accountaccesses.InviteSpecifier;
import io.mailtrap.model.response.accountaccesses.UserSpecifier;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountAccessResponseDeserializerTest {

    private final ObjectMapper mapper = Mapper.get();

    @Test
    void testApiTokenSpecifierDeserialization() throws Exception {
        final String json = """
                  {
                    "id": 1,
                    "specifier_type": "ApiToken",
                    "specifier": {
                      "id": 1,
                      "name": "Token",
                      "token": "xyz",
                      "expires_at": "2025-01-01T00:00:00Z"
                    },
                    "resources": [],
                    "permissions": {
                      "can_read": true,
                      "can_update": true,
                      "can_destroy": true,
                      "can_leave": true
                    }
                  }
                """;

        final AccountAccessResponse response = mapper.readValue(json, AccountAccessResponse.class);

        assertInstanceOf(ApiTokenSpecifier.class, response.getSpecifier());

        final ApiTokenSpecifier apiTokenSpecifier = (ApiTokenSpecifier) response.getSpecifier();
        assertEquals(1, apiTokenSpecifier.getId());
        assertEquals("Token", apiTokenSpecifier.getName());
        assertEquals("xyz", apiTokenSpecifier.getToken());
        assertEquals(OffsetDateTime.parse("2025-01-01T00:00:00Z"), apiTokenSpecifier.getExpiresAt());
    }

    @Test
    void testInviteSpecifierDeserialization() throws Exception {
        final String json = """
                  {
                    "id": 1,
                    "specifier_type": "Invite",
                    "specifier": {
                      "id": 2,
                      "email": "invite@example.com"
                    },
                    "resources": [],
                    "permissions": {
                      "can_read": true,
                      "can_update": true,
                      "can_destroy": true,
                      "can_leave": true
                    }
                  }
                """;

        final AccountAccessResponse response = mapper.readValue(json, AccountAccessResponse.class);

        assertInstanceOf(InviteSpecifier.class, response.getSpecifier());

        final InviteSpecifier inviteSpecifier = (InviteSpecifier) response.getSpecifier();
        assertEquals(2, inviteSpecifier.getId());
        assertEquals("invite@example.com", inviteSpecifier.getEmail());
    }

    @Test
    void testUserSpecifierDeserialization() throws Exception {
        final String json = """
                       {
                         "id": 3,
                         "specifier_type": "User",
                         "specifier": {
                           "id": 3,
                           "email": "user@example.com",
                           "name": "John Doe",
                           "two_factor_authentication_enabled": false
                         },
                         "resources": [],
                         "permissions": {
                           "can_read": true,
                           "can_update": true,
                           "can_destroy": true,
                           "can_leave": true
                         }
                       }
                """;

        final AccountAccessResponse response = mapper.readValue(json, AccountAccessResponse.class);

        assertInstanceOf(UserSpecifier.class, response.getSpecifier());

        final UserSpecifier userSpecifier = (UserSpecifier) response.getSpecifier();
        assertEquals(3, userSpecifier.getId());
        assertEquals("user@example.com", userSpecifier.getEmail());
        assertEquals("John Doe", userSpecifier.getName());
        assertFalse(userSpecifier.isTwoFactorAuthenticationEnabled());
    }

    @Test
    void testMissingFields() {
        final String json = """
                       {
                         "id": 3,
                         "specifier_type": "Unknown",
                         "specifier": {
                           "id": 3,
                           "name": "John Doe, The unknown specifier"
                         },
                         "resources": [],
                         "permissions": {
                           "can_read": true,
                           "can_update": true,
                           "can_destroy": true,
                           "can_leave": true
                         }
                       }
                """;

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mapper.readValue(json, AccountAccessResponse.class));
        assertTrue(exception.getMessage().contains("Invalid specifier_type provided"));
    }
}
