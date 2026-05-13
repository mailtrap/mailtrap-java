package io.mailtrap.model.request.apitokens;

import io.mailtrap.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiTokenRequest extends AbstractModel {

    private String name;

    private List<ApiTokenResource> resources;

}
