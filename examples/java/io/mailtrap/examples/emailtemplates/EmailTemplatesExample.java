package io.mailtrap.examples.emailtemplates;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emailtemplates.CreateEmailTemplateRequest;
import io.mailtrap.model.request.emailtemplates.EmailTemplate;
import io.mailtrap.model.request.emailtemplates.UpdateEmailTemplateRequest;
import io.mailtrap.model.response.emailtemplates.EmailTemplateResponse;

import java.util.List;

public class EmailTemplatesExample {

  private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
  private static final long ACCOUNT_ID = 1L;
  private static final String EMAIL_TEMPLATE_NAME = "My Email Template";
  private static final String EMAIL_TEMPLATE_CATEGORY = "Promotion";
  private static final String EMAIL_TEMPLATE_SUBJECT = "Promotion Template subject";
  private static final String EMAIL_TEMPLATE_BODY_TEXT = "Promotion Text body";
  private static final String EMAIL_TEMPLATE_BODY_HTML = "<div>Promotion body</div>";
  private static final String UPDATED_EMAIL_TEMPLATE_NAME = "My Updated Email Template";

  public static void main(String[] args) {
    final var config = new MailtrapConfig.Builder()
        .token(TOKEN)
        .build();

    final var client = MailtrapClientFactory.createMailtrapClient(config);

    final var createRequest = new CreateEmailTemplateRequest(
        new EmailTemplate(
            EMAIL_TEMPLATE_NAME,
            EMAIL_TEMPLATE_CATEGORY,
            EMAIL_TEMPLATE_SUBJECT,
            EMAIL_TEMPLATE_BODY_TEXT,
            EMAIL_TEMPLATE_BODY_HTML
        ));

    final var createdEmailTemplate = client.emailTemplatesApi().emailTemplates()
        .createEmailTemplate(ACCOUNT_ID, createRequest);

    System.out.println(createdEmailTemplate);

    final var allTemplates = client.emailTemplatesApi().emailTemplates()
        .getAllTemplates(ACCOUNT_ID);

    System.out.println(allTemplates);

    final var emailTemplate = client.emailTemplatesApi().emailTemplates()
        .getEmailTemplate(ACCOUNT_ID, createdEmailTemplate.getId());

    System.out.println(emailTemplate);

    final var updateRequest = new UpdateEmailTemplateRequest(
        new EmailTemplate(
            UPDATED_EMAIL_TEMPLATE_NAME,
            EMAIL_TEMPLATE_CATEGORY,
            EMAIL_TEMPLATE_SUBJECT,
            EMAIL_TEMPLATE_BODY_TEXT,
            EMAIL_TEMPLATE_BODY_HTML
        ));

    final var updatedEmailTemplate = client.emailTemplatesApi().emailTemplates()
        .updateEmailTemplate(ACCOUNT_ID, createdEmailTemplate.getId(), updateRequest);

    System.out.println(updatedEmailTemplate);

    client.emailTemplatesApi().emailTemplates()
        .deleteEmailTemplate(ACCOUNT_ID, updatedEmailTemplate.getId());
  }
}
