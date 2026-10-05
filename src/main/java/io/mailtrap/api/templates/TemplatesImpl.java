package io.mailtrap.api.templates;

import io.mailtrap.Constants;
import io.mailtrap.MailtrapValidator;
import io.mailtrap.api.apiresource.ApiResourceWithValidation;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.templates.CreateTemplateRequest;
import io.mailtrap.model.request.templates.UpdateTemplateRequest;
import io.mailtrap.model.response.templates.TemplateListResponse;
import io.mailtrap.model.response.templates.TemplateResponse;

import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class TemplatesImpl extends ApiResourceWithValidation implements Templates {

    private static final String BASE_PATH = "/api/accounts/%d/templates";

    public TemplatesImpl(final MailtrapConfig config, final MailtrapValidator mailtrapValidator) {
        super(config, mailtrapValidator);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public TemplateListResponse getTemplates(final long accountId, final TemplateListFilter filter) {
        final var queryParams = RequestData.buildQueryParams(
            entry("per_page", Optional.ofNullable(filter).map(TemplateListFilter::getPerPage)),
            entry("token", Optional.ofNullable(filter).map(TemplateListFilter::getToken))
        );

        return httpClient.get(
            String.format(apiHost + BASE_PATH, accountId),
            new RequestData(queryParams),
            TemplateListResponse.class
        );
    }

    @Override
    public TemplateResponse createTemplate(final long accountId, final CreateTemplateRequest request) {
        validateRequestBodyAndThrowException(request);

        return httpClient.post(
            String.format(apiHost + BASE_PATH, accountId),
            request,
            new RequestData(),
            TemplateResponse.class
        );
    }

    @Override
    public TemplateResponse getTemplate(final long accountId, final long templateId) {
        return httpClient.get(
            String.format(apiHost + BASE_PATH + "/%d", accountId, templateId),
            new RequestData(),
            TemplateResponse.class
        );
    }

    @Override
    public TemplateResponse updateTemplate(final long accountId, final long templateId, final UpdateTemplateRequest request) {
        validateRequestBodyAndThrowException(request);

        return httpClient.patch(
            String.format(apiHost + BASE_PATH + "/%d", accountId, templateId),
            request,
            new RequestData(),
            TemplateResponse.class
        );
    }

    @Override
    public void deleteTemplate(final long accountId, final long templateId) {
        httpClient.delete(
            String.format(apiHost + BASE_PATH + "/%d", accountId, templateId),
            new RequestData(),
            Void.class
        );
    }
}
