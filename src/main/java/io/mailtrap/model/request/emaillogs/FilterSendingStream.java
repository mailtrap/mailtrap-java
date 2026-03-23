package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter for sending_stream. Value typically "transactional" or "bulk"; may be single or list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterSendingStream implements EmailLogFilter {

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
