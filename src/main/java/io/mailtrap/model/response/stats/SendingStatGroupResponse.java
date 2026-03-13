package io.mailtrap.model.response.stats;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

@Data
public class SendingStatGroupResponse {

    private String name;
    private Object value;
    private SendingStatsResponse stats;

    @JsonAnySetter
    public void setDynamicField(String key, Object value) {
        if (!"stats".equals(key)) {
            this.name = key;
            this.value = value;
        }
    }
}
