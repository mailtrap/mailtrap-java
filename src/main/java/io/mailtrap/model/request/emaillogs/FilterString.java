package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter with string operators (e.g. client_ip, sending_ip).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterString implements EmailLogFilter {

    public enum Operator {
        equal, not_equal, contain, not_contain
    }

    private Operator operator;
    private String value;

    @Override
    public String getOperatorString() {
        return operator == null ? null : operator.name();
    }

    @Override
    public Object getValue() {
        return value;
    }
}
