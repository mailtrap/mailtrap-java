package io.mailtrap.model.request.emaillogs;

import io.mailtrap.model.response.emaillogs.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter for status. Value may be single or list for equal, not_equal.
 * Use {@link MessageStatus} for type-safe status values.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterStatus implements EmailLogFilter {

    public enum Operator {
        equal, not_equal
    }

    private Operator operator;
    /** {@link MessageStatus}, List of {@link MessageStatus}, or string value. */
    private Object value;

    /** Convenience constructor for a single status. */
    public FilterStatus(Operator operator, MessageStatus status) {
        this.operator = operator;
        this.value = status;
    }

    /** Convenience constructor for multiple statuses. */
    public FilterStatus(Operator operator, List<MessageStatus> statuses) {
        this.operator = operator;
        this.value = statuses;
    }

    @Override
    public String getOperatorString() {
        return operator == null ? null : operator.name();
    }

    @Override
    public Object getValue() {
        if (value instanceof MessageStatus) {
            return ((MessageStatus) value).getValue();
        }
        if (value instanceof List<?> list) {
            return list.stream()
                    .map(v -> v instanceof MessageStatus ? ((MessageStatus) v).getValue() : String.valueOf(v))
                    .collect(Collectors.toList());
        }
        return value;
    }
}
