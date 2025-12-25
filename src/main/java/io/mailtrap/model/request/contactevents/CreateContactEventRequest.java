package io.mailtrap.model.request.contactevents;

import io.mailtrap.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CreateContactEventRequest extends AbstractModel {
    private String name;
    private Map<String, Object> params;
}
