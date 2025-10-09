package io.mailtrap.model.request.contactexports;

import io.mailtrap.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateContactsExportRequest extends AbstractModel {

    private List<ContactExportFilter> filters;
}
