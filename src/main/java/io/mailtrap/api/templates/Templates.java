package io.mailtrap.api.templates;

import io.mailtrap.model.request.templates.CreateTemplateRequest;
import io.mailtrap.model.request.templates.UpdateTemplateRequest;
import io.mailtrap.model.response.templates.TemplateListResponse;
import io.mailtrap.model.response.templates.TemplateResponse;

public interface Templates {

    /**
     * Get a page of templates existing in your account
     *
     * @param accountId unique account ID
     * @param filter    pagination parameters, may be {@code null}
     * @return templates of the requested page with pagination metadata
     */
    TemplateListResponse getTemplates(long accountId, TemplateListFilter filter);

    /**
     * Create a new template
     *
     * @param accountId unique account ID
     * @param request   template create payload
     * @return created template
     */
    TemplateResponse createTemplate(long accountId, CreateTemplateRequest request);

    /**
     * Get a template by ID
     *
     * @param accountId  unique account ID
     * @param templateId unique template ID
     * @return template attributes
     */
    TemplateResponse getTemplate(long accountId, long templateId);

    /**
     * Update a template
     *
     * @param accountId  unique account ID
     * @param templateId unique template ID
     * @param request    template update payload, only the provided fields are changed
     * @return updated template
     */
    TemplateResponse updateTemplate(long accountId, long templateId, UpdateTemplateRequest request);

    /**
     * Delete a template
     *
     * @param accountId  unique account ID
     * @param templateId unique template ID
     */
    void deleteTemplate(long accountId, long templateId);

}
