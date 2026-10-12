package io.mailtrap.model.response.templates;

import io.mailtrap.model.response.Pagination;
import lombok.Data;

import java.util.List;

/**
 * Paginated list of templates, wrapped as {@code data} alongside page-token pagination
 * metadata.
 */
@Data
public class TemplateListResponse {

    private List<Template> data;

    private Pagination pagination;

}
