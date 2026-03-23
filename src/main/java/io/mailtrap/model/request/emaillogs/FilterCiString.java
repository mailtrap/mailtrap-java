package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter with case-insensitive string operators (e.g. to, from). Value may be single or list for ci_equal, ci_not_equal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCiString implements EmailLogFilter {

    public enum Operator {
        ci_contain, ci_not_contain, ci_equal, ci_not_equal
    }

    private Operator operator;
    private Object value;

    @Override
    public String getOperatorString() {
        return operator == null ? null : operator.name();
    }

    @Override
    public Object getValue() {
        return value;
    }
}
