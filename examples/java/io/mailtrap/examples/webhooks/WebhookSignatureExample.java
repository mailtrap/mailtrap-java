package io.mailtrap.examples.webhooks;

import io.mailtrap.webhooks.WebhookSignatures;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class WebhookSignatureExample {

    public static void main(final String[] args) throws Exception {
        // --- Direct verification (e.g. for unit tests or custom routers) ----
        final String payload = "{\"event\":\"delivery\",\"message_id\":\"abc-123\"}";
        final String signingSecret = "8d9a3c0e7f5b2d4a6c1e9f8b3a7d5c2e";

        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(signingSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        final String signature = HexFormat.of().formatHex(
            mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));

        assertTrue(WebhookSignatures.verify(payload, signature, signingSecret));

        // Bad input never throws — it returns false:
        assertFalse(WebhookSignatures.verify(payload, "not-hex", signingSecret));
        assertFalse(WebhookSignatures.verify(payload, "", signingSecret));
    }

    private static void assertTrue(final boolean condition) {
        if (!condition) {
            throw new AssertionError("expected true");
        }
    }

    private static void assertFalse(final boolean condition) {
        if (condition) {
            throw new AssertionError("expected false");
        }
    }
}
