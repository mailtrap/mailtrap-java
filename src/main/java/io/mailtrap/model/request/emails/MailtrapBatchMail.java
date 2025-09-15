package io.mailtrap.model.request.emails;

import io.mailtrap.model.AbstractModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MailtrapBatchMail extends AbstractModel {

    @Valid
    private BatchEmailBase base;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<MailtrapMail> requests;

}
