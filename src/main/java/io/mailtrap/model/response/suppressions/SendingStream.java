package io.mailtrap.model.response.suppressions;

/**
 * @deprecated Use {@link io.mailtrap.model.SendingStream} instead.
 *             This class will be removed in the next major release.
 */
@Deprecated
public class SendingStream {

    private SendingStream() {
    }

    public static io.mailtrap.model.SendingStream ANY = io.mailtrap.model.SendingStream.ANY;
    public static io.mailtrap.model.SendingStream TRANSACTIONAL = io.mailtrap.model.SendingStream.TRANSACTIONAL;
    public static io.mailtrap.model.SendingStream BULK = io.mailtrap.model.SendingStream.BULK;

    public static io.mailtrap.model.SendingStream fromValue(String value) {
        return io.mailtrap.model.SendingStream.fromValue(value);
    }
}
