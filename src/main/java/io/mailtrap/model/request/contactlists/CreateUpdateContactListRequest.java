package io.mailtrap.model.request.contactlists;

import io.mailtrap.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUpdateContactListRequest extends AbstractModel {
    private String name;
}
