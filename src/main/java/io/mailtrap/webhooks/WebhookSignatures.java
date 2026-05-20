package io.mailtrap.webhooks;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Helpers for verifying inbound Mailtrap webhook signatures.
 *
 * <p>Mailtrap signs every outbound webhook by computing
 * {@code HMAC-SHA256(signing_secret, raw_request_body)} and sending the
 * lowercase hex digest in the {@code Mailtrap-Signature} HTTP header. To
 * authenticate a webhook on the receiver side, compute the same digest using
 * the {@code signing_secret} returned when the webhook was created and compare
 * it to the value of the header in constant time.
 *
 * <p>The comparison is performed with {@link MessageDigest#isEqual(byte[], byte[])}
 * to avoid timing side-channels.
 *
 * <p>The method never throws on inputs that could plausibly arrive over the
 * wire (empty strings, wrong-length signatures, non-hex characters, missing
 * secret) — it simply returns {@code false}. This makes it safe to call
 * directly from a request handler without wrapping in try/catch.
 *
 * @see <a href="https://docs.mailtrap.io/email-api-smtp/advanced/webhooks#verifying-the-signature">Mailtrap docs — Verifying the signature</a>
 */
public final class WebhookSignatures {

    /**
     * Hex-encoded HMAC-SHA256 signature length (SHA-256 produces 32 bytes / 64 hex chars).
     */
    public static final int SIGNATURE_HEX_LENGTH = 64;

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private WebhookSignatures() {
        // utility class — not instantiable
    }

    /**
     * Verifies the HMAC-SHA256 signature of a Mailtrap webhook payload.
     *
     * @param payload       the raw request body, exactly as received. <strong>Do not</strong>
     *                      parse and re-serialize the JSON — re-encoding may reorder keys or
     *                      alter whitespace and invalidate the signature. With Spring use
     *                      {@code @RequestBody byte[]} or read the body directly from
     *                      {@code HttpServletRequest.getInputStream()} on the webhook route
     *                      so the body is preserved verbatim.
     * @param signature     the value of the {@code Mailtrap-Signature} HTTP header
     *                      (lowercase hex string).
     * @param signingSecret the webhook's {@code signing_secret}, returned by the Webhooks API
     *                      on webhook creation.
     * @return {@code true} if the signature is valid for the given payload and secret,
     *         {@code false} otherwise (including any {@code null}/empty input,
     *         wrong-length or non-hex signatures).
     */
    public static boolean verify(final String payload, final String signature, final String signingSecret) {
        if (signature == null || signature.isEmpty()) {
            return false;
        }
        if (signingSecret == null || signingSecret.isEmpty()) {
            return false;
        }
        if (payload == null || payload.isEmpty()) {
            return false;
        }
        if (signature.length() != SIGNATURE_HEX_LENGTH) {
            return false;
        }

        final byte[] providedBytes;
        try {
            providedBytes = HexFormat.of().parseHex(signature);
        } catch (final IllegalArgumentException e) {
            // Non-hex characters in the provided signature — reject without throwing.
            return false;
        }

        final byte[] expectedBytes;
        try {
            final Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(signingSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            expectedBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        } catch (final NoSuchAlgorithmException e) {
            // HmacSHA256 is required by every standards-conformant JVM (JCA spec). This
            // branch is unreachable in practice — treat it as a fatal misconfiguration.
            throw new IllegalStateException("HmacSHA256 algorithm is not available in this JVM", e);
        } catch (final java.security.InvalidKeyException e) {
            // SecretKeySpec rejects only zero-length keys, which we already guard above.
            // Any other InvalidKeyException would indicate a JVM/provider bug.
            throw new IllegalStateException("Failed to initialize HmacSHA256 with the provided signing secret", e);
        }

        // Guard the byte-length first — MessageDigest.isEqual is constant-time only when
        // the inputs have the same length, and we already enforced this via the hex-length
        // check above, but reassert defensively in case SIGNATURE_HEX_LENGTH ever changes.
        if (expectedBytes.length != providedBytes.length) {
            return false;
        }

        return MessageDigest.isEqual(expectedBytes, providedBytes);
    }
}
