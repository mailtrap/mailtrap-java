package io.mailtrap.api.templates;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.templates.CreateTemplateRequest;
import io.mailtrap.model.request.templates.UpdateTemplateRequest;
import io.mailtrap.model.response.templates.TemplateListResponse;
import io.mailtrap.model.response.templates.TemplateResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplatesImplTest extends BaseTest {

    private static final String TOO_LONG_NAME = "x".repeat(256);

    private final String basePath = Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/templates";

    private Templates api;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(basePath,
                        "GET", null, "api/templates/listTemplatesResponse.json"),

                DataMock.build(basePath,
                        "GET", null, "api/templates/listTemplatesResponse.json", Map.of("per_page", 10, "token", 2)),

                DataMock.build(basePath,
                        "POST", "api/templates/createTemplateRequest.json", "api/templates/createTemplateResponse.json"),

                DataMock.build(basePath + "/" + emailTemplateId,
                        "GET", null, "api/templates/getTemplateResponse.json"),

                DataMock.build(basePath + "/" + emailTemplateId,
                        "PATCH", "api/templates/updateTemplateRequest.json", "api/templates/updateTemplateResponse.json"),

                DataMock.build(basePath + "/" + emailTemplateId,
                        "DELETE", null, null)
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).templatesApi().templates();
    }

    @Test
    void test_getTemplates() {
        final TemplateListResponse response = api.getTemplates(accountId, null);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(emailTemplateId, response.getData().get(0).getId());
        assertEquals("Promotion", response.getData().get(0).getCategory());

        assertNotNull(response.getPagination());
        assertEquals(1, response.getPagination().getToken());
        assertNull(response.getPagination().getPrevToken());
        assertEquals(2, response.getPagination().getNextToken());
    }

    @Test
    void test_getTemplates_withPagination() {
        final TemplateListResponse response = api.getTemplates(accountId,
                TemplateListFilter.builder().perPage(10).token(2).build());

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(2, response.getPagination().getNextToken());
    }

    @Test
    void test_createTemplate() {
        final CreateTemplateRequest request = CreateTemplateRequest.builder()
                .name("My Template")
                .category("Promotion")
                .subject("Promotion Template subject")
                .bodyText("Promotion Text body")
                .bodyHtml("<div>Promotion body</div>")
                .build();

        final TemplateResponse created = api.createTemplate(accountId, request);

        assertNotNull(created);
        assertEquals(emailTemplateId, created.getData().getId());
        assertEquals("My Template", created.getData().getName());
    }

    @Test
    void test_createTemplate_shouldFailOnValidationFieldSize() {
        final CreateTemplateRequest request = CreateTemplateRequest.builder()
                .name(TOO_LONG_NAME)
                .category("Promotion")
                .subject("Promotion Template subject")
                .build();

        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class,
                () -> api.createTemplate(accountId, request));

        assertTrue(exception.getMessage().contains("name"));
        assertTrue(exception.getMessage().contains("size must be between 1 and 255"));
    }

    @Test
    void test_createTemplate_shouldFailOnMissingRequiredFields() {
        final CreateTemplateRequest request = CreateTemplateRequest.builder().name("My Template").build();

        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class,
                () -> api.createTemplate(accountId, request));

        assertTrue(exception.getMessage().contains("category=must not be null"));
        assertTrue(exception.getMessage().contains("subject=must not be null"));
    }

    @Test
    void test_getTemplate() {
        final TemplateResponse response = api.getTemplate(accountId, emailTemplateId);

        assertNotNull(response);
        assertEquals(emailTemplateId, response.getData().getId());
        assertEquals("Promotion Template", response.getData().getName());
    }

    @Test
    void test_updateTemplate() {
        final UpdateTemplateRequest request = UpdateTemplateRequest.builder()
                .name("My Updated Template")
                .subject("Promotion Template subject")
                .build();

        final TemplateResponse updated = api.updateTemplate(accountId, emailTemplateId, request);

        assertNotNull(updated);
        assertEquals(emailTemplateId, updated.getData().getId());
        assertEquals("My Updated Template", updated.getData().getName());
    }

    @Test
    void test_updateTemplate_shouldFailOnValidationFieldSize() {
        final UpdateTemplateRequest request = UpdateTemplateRequest.builder().subject(TOO_LONG_NAME).build();

        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class,
                () -> api.updateTemplate(accountId, emailTemplateId, request));

        assertTrue(exception.getMessage().contains("subject"));
        assertTrue(exception.getMessage().contains("size must be between 1 and 255"));
    }

    @Test
    void test_deleteTemplate() {
        assertDoesNotThrow(() -> api.deleteTemplate(accountId, emailTemplateId));
    }

}
