package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter with exact match (e.g. category, email_service_provider). Value may be single or list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterExactString implements EmailLogFilter {

    public enum Operator {
        equal, not_equal
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
