package io.mailtrap.examples.templates;

import io.mailtrap.api.templates.TemplateListFilter;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.templates.CreateTemplateRequest;
import io.mailtrap.model.request.templates.UpdateTemplateRequest;

public class TemplatesExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));
    private static final String TEMPLATE_NAME = "My Template";
    private static final String TEMPLATE_CATEGORY = "Promotion";
    private static final String TEMPLATE_SUBJECT = "Promotion Template subject";
    private static final String TEMPLATE_BODY_TEXT = "Promotion Text body";
    private static final String TEMPLATE_BODY_HTML = "<div>Promotion body</div>";
    private static final String UPDATED_TEMPLATE_NAME = "My Updated Template";

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final var client = MailtrapClientFactory.createMailtrapClient(config);
        final var templates = client.templatesApi().templates();

        final var createRequest = CreateTemplateRequest.builder()
            .name(TEMPLATE_NAME)
            .category(TEMPLATE_CATEGORY)
            .subject(TEMPLATE_SUBJECT)
            .bodyText(TEMPLATE_BODY_TEXT)
            .bodyHtml(TEMPLATE_BODY_HTML)
            .build();

        final var created = templates.createTemplate(ACCOUNT_ID, createRequest).getData();

        System.out.println(created);

        final var page = templates.getTemplates(ACCOUNT_ID, TemplateListFilter.builder().perPage(50).token(1).build());

        System.out.println(page.getData());
        System.out.println(page.getPagination());

        final var template = templates.getTemplate(ACCOUNT_ID, created.getId()).getData();

        System.out.println(template);

        final var updateRequest = UpdateTemplateRequest.builder()
            .name(UPDATED_TEMPLATE_NAME)
            .build();

        final var updated = templates.updateTemplate(ACCOUNT_ID, created.getId(), updateRequest).getData();

        System.out.println(updated);

        templates.deleteTemplate(ACCOUNT_ID, updated.getId());
    }
}
