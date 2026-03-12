package io.mailtrap.model.request.emaillogs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterNumber implements EmailLogFilter {

    public enum Operator {
        equal, greater_than, less_than
    }

    private Operator operator;
    private Integer value;

    @Override
    public String getOperatorString() {
        return operator == null ? null : operator.name();
    }

    @Override
    public Object getValue() {
        return value;
    }
}
