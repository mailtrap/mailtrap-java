package io.mailtrap.model.response.emails;

import lombok.Data;

import java.util.List;

@Data
public class BatchSendResponse {

    private boolean success;

    private List<BatchSendDetails> responses;

    private List<String> errors;

}
