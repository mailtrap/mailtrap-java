package io.mailtrap.webhooks;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebhookSignaturesTest {

    // ---------------------------------------------------------------------
    // Cross-SDK shared fixture — DO NOT CHANGE.
    //
    // The same (payload, signing_secret, expected_signature) triple is
    // embedded verbatim in the test suites of every official Mailtrap SDK
    // (Ruby, Python, PHP, Node.js, Java, .NET) to guarantee byte-for-byte
    // compatibility of the verification algorithm across languages. Keep
    // these three strings in sync with the other SDKs.
    // ---------------------------------------------------------------------
    private static final String FIXTURE_PAYLOAD =
        "{\"event\":\"delivery\",\"sending_stream\":\"transactional\",\"category\":\"welcome\","
            + "\"message_id\":\"a8b1d8f6-1f8d-4a3c-9b2e-1a2b3c4d5e6f\","
            + "\"email\":\"recipient@example.com\","
            + "\"event_id\":\"f1e2d3c4-b5a6-7890-1234-567890abcdef\","
            + "\"timestamp\":1716070000}";
    private static final String FIXTURE_SIGNING_SECRET = "8d9a3c0e7f5b2d4a6c1e9f8b3a7d5c2e";
    private static final String FIXTURE_EXPECTED_SIGNATURE =
        "6d262e2611cd09be1f948382b5c611d63b0e585c4c9c5e40139d6ac3876d5433";

    // ---------------------------------------------------------------------
    // 1. Valid signature → true
    // ---------------------------------------------------------------------
    @Test
    void verify_withValidSignature_returnsTrue() {
        assertTrue(WebhookSignatures.verify(FIXTURE_PAYLOAD, FIXTURE_EXPECTED_SIGNATURE, FIXTURE_SIGNING_SECRET));
    }

    // ---------------------------------------------------------------------
    // 2. Wrong secret → false
    // ---------------------------------------------------------------------
    @Test
    void verify_withWrongSecret_returnsFalse() {
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, FIXTURE_EXPECTED_SIGNATURE, "wrong_secret_value"));
    }

    // ---------------------------------------------------------------------
    // 3. Payload tampered (one byte changed) → false
    // ---------------------------------------------------------------------
    @Test
    void verify_withTamperedPayload_returnsFalse() {
        // Flip "delivery" to "delivere" — same length, different bytes.
        final String tampered = FIXTURE_PAYLOAD.replace("\"delivery\"", "\"delivere\"");
        assertFalse(WebhookSignatures.verify(tampered, FIXTURE_EXPECTED_SIGNATURE, FIXTURE_SIGNING_SECRET));
    }

    // ---------------------------------------------------------------------
    // 4. Signature with wrong length → false (no throw)
    // ---------------------------------------------------------------------
    @Test
    void verify_withSignatureOfWrongLength_returnsFalse() {
        final String tooShort = FIXTURE_EXPECTED_SIGNATURE.substring(0, 63);
        final String tooLong = FIXTURE_EXPECTED_SIGNATURE + "a";

        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, tooShort, FIXTURE_SIGNING_SECRET));
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, tooLong, FIXTURE_SIGNING_SECRET));
    }

    // ---------------------------------------------------------------------
    // 5. Signature with non-hex characters → false (no throw)
    // ---------------------------------------------------------------------
    @Test
    void verify_withNonHexCharactersInSignature_returnsFalse() {
        // Same length (64), but contains 'z' which is not a hex digit.
        final String nonHex = "z" + FIXTURE_EXPECTED_SIGNATURE.substring(1);
        assertEquals(SIGNATURE_HEX_LENGTH(), nonHex.length());
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, nonHex, FIXTURE_SIGNING_SECRET));
    }

    // ---------------------------------------------------------------------
    // 6. Empty signature string → false
    // ---------------------------------------------------------------------
    @Test
    void verify_withEmptySignature_returnsFalse() {
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, "", FIXTURE_SIGNING_SECRET));
    }

    // ---------------------------------------------------------------------
    // 7. Empty signingSecret → false
    // ---------------------------------------------------------------------
    @Test
    void verify_withEmptySigningSecret_returnsFalse() {
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, FIXTURE_EXPECTED_SIGNATURE, ""));
    }

    // ---------------------------------------------------------------------
    // 8. Empty payload with non-empty signature → false
    // ---------------------------------------------------------------------
    @Test
    void verify_withEmptyPayload_returnsFalse() {
        assertFalse(WebhookSignatures.verify("", FIXTURE_EXPECTED_SIGNATURE, FIXTURE_SIGNING_SECRET));
    }

    // ---------------------------------------------------------------------
    // 9. Known-good fixture round-trip — independently recompute the HMAC
    //    in the test (not via the helper) and assert it matches both the
    //    embedded expected signature AND the helper's verdict.
    // ---------------------------------------------------------------------
    @Test
    void verify_fixtureRoundTrip_matchesIndependentlyComputedHmac() throws Exception {
        // Recompute the HMAC-SHA256 independently of the helper, using the JDK
        // primitives directly. If this drifts from FIXTURE_EXPECTED_SIGNATURE,
        // either the fixture is wrong or the algorithm/encoding has changed.
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(
            FIXTURE_SIGNING_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        final byte[] digest = mac.doFinal(FIXTURE_PAYLOAD.getBytes(StandardCharsets.UTF_8));
        final String computedHex = HexFormat.of().formatHex(digest);

        assertEquals(FIXTURE_EXPECTED_SIGNATURE, computedHex,
            "Independently computed HMAC must equal embedded fixture signature");

        assertTrue(WebhookSignatures.verify(FIXTURE_PAYLOAD, FIXTURE_EXPECTED_SIGNATURE, FIXTURE_SIGNING_SECRET),
            "Helper must agree the fixture is valid");
    }

    // ---------------------------------------------------------------------
    // Bonus: null inputs → false (no NullPointerException)
    // ---------------------------------------------------------------------
    @Test
    void verify_withNullPayload_returnsFalse() {
        assertFalse(WebhookSignatures.verify(null, FIXTURE_EXPECTED_SIGNATURE, FIXTURE_SIGNING_SECRET));
    }

    @Test
    void verify_withNullSignature_returnsFalse() {
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, null, FIXTURE_SIGNING_SECRET));
    }

    @Test
    void verify_withNullSigningSecret_returnsFalse() {
        assertFalse(WebhookSignatures.verify(FIXTURE_PAYLOAD, FIXTURE_EXPECTED_SIGNATURE, null));
    }

    private static int SIGNATURE_HEX_LENGTH() {
        return WebhookSignatures.SIGNATURE_HEX_LENGTH;
    }
}
