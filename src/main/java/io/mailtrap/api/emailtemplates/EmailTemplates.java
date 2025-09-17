package io.mailtrap.api.emailtemplates;

import io.mailtrap.model.request.emailtemplates.CreateEmailTemplateRequest;
import io.mailtrap.model.request.emailtemplates.UpdateEmailTemplateRequest;
import io.mailtrap.model.response.emailtemplates.EmailTemplateResponse;

import java.util.List;

public interface EmailTemplates {

    /**
     * Get all email templates existing in your account
     *
     * @param accountId unique account ID
     * @return list of existing email templates
     */
    List<EmailTemplateResponse> getAllTemplates(long accountId);

    /**
     * Create a new email template
     *
     * @param accountId unique account ID
     * @param request   email template create payload
     * @return created email template
     */
    EmailTemplateResponse createEmailTemplate(long accountId, CreateEmailTemplateRequest request);

    /**
     * Get an email template by ID
     *
     * @param accountId       unique account ID
     * @param emailTemplateId unique email template ID
     * @return email template attributes
     */
    EmailTemplateResponse getEmailTemplate(long accountId, long emailTemplateId);

    /**
     * Update an email template
     *
     * @param accountId       unique account ID
     * @param emailTemplateId unique email template ID
     * @param request         email template update payload
     * @return updated email template
     */
    EmailTemplateResponse updateEmailTemplate(long accountId, long emailTemplateId, UpdateEmailTemplateRequest request);

    /**
     * Delete an email template
     *
     * @param accountId       unique account ID
     * @param emailTemplateId unique email template ID
     */
    void deleteEmailTemplate(long accountId, long emailTemplateId);

}
