package io.mailtrap;

/**
 * Utility class containing constant values used in the SDK.
 */
public final class Constants {
    private Constants() {
    }

    public static final String EMAIL_SANDBOX_SEND_HOST = "https://sandbox.api.mailtrap.io";

    /**
     * @deprecated Use {@link #EMAIL_SANDBOX_SEND_HOST} instead. Will be removed in a future major release.
     */
    @Deprecated(since = "1.11.0")
    public static final String EMAIL_TESTING_SEND_HOST = EMAIL_SANDBOX_SEND_HOST;

    public static final String EMAIL_SENDING_SEND_HOST = "https://send.api.mailtrap.io";
    public static final String BULK_SENDING_HOST = "https://bulk.api.mailtrap.io";
    public static final String GENERAL_HOST = "https://mailtrap.io";

}
