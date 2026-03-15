package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter for optional string (e.g. subject). Value optional for empty, not_empty.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterOptionalString implements EmailLogFilter {

    public enum Operator {
        ci_contain, ci_not_contain, ci_equal, ci_not_equal, empty, not_empty
    }

    private Operator operator;
    private String value;

    /** Constructor for operators that do not require a value (e.g. empty, not_empty). */
    public FilterOptionalString(Operator operator) {
        this.operator = operator;
        this.value = null;
    }

    @Override
    public String getOperatorString() {
        return operator == null ? null : operator.name();
    }

    @Override
    public Object getValue() {
        return value;
    }
}
