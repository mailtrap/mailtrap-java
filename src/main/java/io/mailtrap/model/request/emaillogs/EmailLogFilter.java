package io.mailtrap.model.request.emaillogs;

/**
 * Common contract for email log filters so the API can serialize operator and value.
 */
public interface EmailLogFilter {

    /** API operator string (e.g. "equal", "ci_contain"). */
    String getOperatorString();

    /** Filter value; may be null for operators like empty/not_empty. */
    Object getValue();
}
