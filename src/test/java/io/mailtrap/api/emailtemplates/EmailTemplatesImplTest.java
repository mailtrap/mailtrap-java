package io.mailtrap.api.emailtemplates;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emailtemplates.CreateEmailTemplateRequest;
import io.mailtrap.model.request.emailtemplates.EmailTemplate;
import io.mailtrap.model.request.emailtemplates.UpdateEmailTemplateRequest;
import io.mailtrap.model.response.emailtemplates.EmailTemplateResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplatesImplTest extends BaseTest {

  private EmailTemplates api;

  @BeforeEach
  public void init() {
    final TestHttpClient httpClient = new TestHttpClient(List.of(
        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_templates",
            "GET", null, "api/emailtemplates/getAllEmailTemplatesResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_templates",
            "POST", "api/emailtemplates/createEmailTemplateRequest.json", "api/emailtemplates/createEmailTemplateResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_templates/" + emailTemplateId,
            "GET", null, "api/emailtemplates/getEmailTemplateResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_templates/" + emailTemplateId,
            "PATCH", "api/emailtemplates/updateEmailTemplateRequest.json", "api/emailtemplates/updateEmailTemplateResponse.json"),

        DataMock.build(Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/email_templates/" + emailTemplateId,
            "DELETE", null, null)
    ));

    final MailtrapConfig testConfig = new MailtrapConfig.Builder()
        .httpClient(httpClient)
        .token("dummy_token")
        .build();

    api = MailtrapClientFactory.createMailtrapClient(testConfig).emailTemplatesApi().emailTemplates();
  }

  @Test
  void test_getAllTemplates() {
    final List<EmailTemplateResponse> allTemplates = api.getAllTemplates(accountId);

    assertEquals(2, allTemplates.size());
    assertEquals(emailTemplateId, allTemplates.get(0).getId());
    assertEquals("Promotion", allTemplates.get(0).getCategory());
  }

  @Test
  void test_createEmailTemplate() {
    final CreateEmailTemplateRequest request = new CreateEmailTemplateRequest(
        new EmailTemplate(
            "My Email Template",
            "Promotion",
            "Promotion Template subject",
            "Promotion Text body",
            "<div>Promotion body</div>"));

    final EmailTemplateResponse created = api.createEmailTemplate(accountId, request);

    assertNotNull(created);
    assertEquals(emailTemplateId, created.getId());
    assertEquals("My Email Template", created.getName());
  }

  @Test
  void test_createEmailTemplate_shouldFailOnValidationFieldSize() {
    final CreateEmailTemplateRequest request = new CreateEmailTemplateRequest(
        new EmailTemplate(
            "1s9QhvF587zZkPQjGkyhtUrFcUDlrN04yAoItmoxiRonVkfjKxhX0GyuMhGh8OxDz2F6mJUExVXWKgX9NdreWSKcDjOEzGpxqUj3E9UOgnyvL75UAV9SSwaolnUniMLQ5gKPfVitS5D8xB3HJ4AE6YbSM72EmXt2qBOBni8dL5atGEKw9FGCa1OO7xyipGeq5sz9dlMMQlQl2n90LF94xntrCnXIEBTiaSXZ055rR73RbLnBAITsyTHxI9Sfy9nPgtpIhmAQwZETfd1t6ZN",
            "Promotion",
            "Promotion Template subject",
            "Promotion Text body",
            "<div>Promotion body</div>"));

    final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> api.createEmailTemplate(accountId, request));

    assertTrue(exception.getMessage().contains("emailTemplate.name"));
    assertTrue(exception.getMessage().contains("size must be between 1 and 255"));
  }

  @Test
  void test_createEmailTemplate_shouldFailOnValidationWithNullableBody() {
    final CreateEmailTemplateRequest request = new CreateEmailTemplateRequest(null);

    final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> api.createEmailTemplate(accountId, request));

    assertTrue(exception.getMessage().contains("Violations: emailTemplate=must not be null"));
  }

  @Test
  void test_getEmailTemplate() {
    final EmailTemplateResponse emailTemplate = api.getEmailTemplate(accountId, emailTemplateId);

    assertNotNull(emailTemplate);
    assertEquals(emailTemplateId, emailTemplate.getId());
    assertEquals("Promotion Template", emailTemplate.getName());
  }

  @Test
  void test_updateEmailTemplate() {
    final UpdateEmailTemplateRequest request = new UpdateEmailTemplateRequest(
        new EmailTemplate(
            "My Updated Email Template",
            "Promotion",
            "Promotion Template subject",
            "Promotion Text body",
            "<div>Promotion body</div>"));

    final EmailTemplateResponse updated = api.updateEmailTemplate(accountId, emailTemplateId, request);

    assertNotNull(updated);
    assertEquals(emailTemplateId, updated.getId());
    assertEquals("My Updated Email Template", updated.getName());
  }

  @Test
  void test_updateEmailTemplate_shouldFailOnValidationFieldSize() {
    final UpdateEmailTemplateRequest request = new UpdateEmailTemplateRequest(
        new EmailTemplate(
            "My Updated Email Template",
            "Promotion",
            "1s9QhvF587zZkPQjGkyhtUrFcUDlrN04yAoItmoxiRonVkfjKxhX0GyuMhGh8OxDz2F6mJUExVXWKgX9NdreWSKcDjOEzGpxqUj3E9UOgnyvL75UAV9SSwaolnUniMLQ5gKPfVitS5D8xB3HJ4AE6YbSM72EmXt2qBOBni8dL5atGEKw9FGCa1OO7xyipGeq5sz9dlMMQlQl2n90LF94xntrCnXIEBTiaSXZ055rR73RbLnBAITsyTHxI9Sfy9nPgtpIhmAQwZETfd1t6ZN",
            "Promotion Text body",
            "<div>Promotion body</div>"));

    final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> api.updateEmailTemplate(accountId, emailTemplateId, request));

    assertTrue(exception.getMessage().contains("emailTemplate.subject"));
    assertTrue(exception.getMessage().contains("size must be between 1 and 255"));
  }

  @Test
  void test_deleteEmailTemplate() {
    assertDoesNotThrow(() -> api.deleteEmailTemplate(accountId, emailTemplateId));
  }

}
