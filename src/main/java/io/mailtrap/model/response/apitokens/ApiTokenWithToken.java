package io.mailtrap.model.response.apitokens;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiTokenWithToken extends ApiToken {

    private String token;

}
