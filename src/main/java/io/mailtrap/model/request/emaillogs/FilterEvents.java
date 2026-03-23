package io.mailtrap.model.request.emaillogs;

import io.mailtrap.model.response.emaillogs.EmailLogEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter for events. Value may be single or list for include_event,
 * not_include_event.
 * Use {@link EmailLogEventType} for type-safe event values.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterEvents implements EmailLogFilter {

    public enum Operator {
        include_event, not_include_event
    }

    private Operator operator;
    /**
     * {@link EmailLogEventType}, List of {@link EmailLogEventType}, or string
     * value.
     */
    private Object value;

    /** Convenience constructor for a single event type. */
    public FilterEvents(Operator operator, EmailLogEventType eventType) {
        this.operator = operator;
        this.value = eventType;
    }

    /** Convenience constructor for multiple event types. */
    public FilterEvents(Operator operator, List<EmailLogEventType> eventTypes) {
        this.operator = operator;
        this.value = eventTypes;
    }

    @Override
    public String getOperatorString() {
        return operator == null ? null : operator.name();
    }

    @Override
    public Object getValue() {
        if (value instanceof EmailLogEventType) {
            return ((EmailLogEventType) value).getValue();
        }
        if (value instanceof List<?> list) {
            return list.stream()
                    .map(v -> v instanceof EmailLogEventType ? ((EmailLogEventType) v).getValue() : String.valueOf(v))
                    .collect(Collectors.toList());
        }
        return value;
    }
}
