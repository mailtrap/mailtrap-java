package io.mailtrap.model.request.subaccounts;

import io.mailtrap.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubAccountRequest extends AbstractModel {

    private SubAccountInput account;

}
