package io.mailtrap.model.response.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.mailtrap.model.AccessLevel;
import lombok.Data;

import java.util.List;

@Data
public class AccountsResponse {

    private long id;

    private String name;

    /**
     * The token's access levels on the account. A level this SDK version doesn't know is left
     * out.
     */
    @JsonProperty("access_levels")
    @JsonSetter(contentNulls = Nulls.SKIP)
    private List<AccessLevel> accessLevels;

}
