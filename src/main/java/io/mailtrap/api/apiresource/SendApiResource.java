package io.mailtrap.api.apiresource;

import io.mailtrap.CustomValidator;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.model.mailvalidation.ContentView;
import io.mailtrap.model.mailvalidation.MailContentView;
import io.mailtrap.model.mailvalidation.ResolvedMailContentView;
import io.mailtrap.model.mailvalidation.ResolvedMailView;
import io.mailtrap.model.request.emails.BatchEmailBase;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Abstract class representing a resource for sending emails via Mailtrap API.
 */
public abstract class SendApiResource extends ApiResourceWithValidation {

    protected SendApiResource(MailtrapConfig config, CustomValidator customValidator) {
        super(config, customValidator);
    }

    /**
     * Validates the request body of an email message and throws an exception if it is invalid.
     *
     * @param mail The email message to be validated.
     * @throws InvalidRequestBodyException If the request body is invalid.
     */
    protected void validateMailPayload(MailtrapMail mail) {
        if (mail == null) {
            throw new InvalidRequestBodyException("Mail must not be null");
        }

        // Perform bean validation (NotNull, etc.)
        validateRequestBodyAndThrowException(mail);

        // Validate subject/text/html/templateUuid
        validateContentRules(MailContentView.of(mail));
    }

    /**
     * Validates the request body of batch email and throws an exception if it is invalid.
     *
     * @param batch batch request to be validated.
     * @throws InvalidRequestBodyException If the request body is invalid.
     */
    protected void validateBatchPayload(MailtrapBatchMail batch) {
        assertBatchMailNotNull(batch);

        BatchEmailBase base = batch.getBase();

        for (int i = 0; i < batch.getRequests().size(); i++) {
            MailtrapMail mail = batch.getRequests().get(i);
            ResolvedMailView mailView = new ResolvedMailView(base, mail);

            try {
                // Perform bean validation (NotNull, etc.)
                validateRequestBodyAndThrowException(mailView);
            } catch (InvalidRequestBodyException e) {
                throw new InvalidRequestBodyException("requests[" + i + "]: " + e.getMessage(), e);
            }

            validateContentRules(ResolvedMailContentView.of(mailView));

            if (mailView.getFrom() == null) {
                throw new InvalidRequestBodyException("requests[" + i + "]: from is required (either in mail or base)");
            }
        }
    }

    private void assertBatchMailNotNull(MailtrapBatchMail batchMail) {
        if (batchMail == null) {
            throw new InvalidRequestBodyException("BatchMail must not be null");
        }
        if (batchMail.getRequests() == null || batchMail.getRequests().isEmpty()) {
            throw new InvalidRequestBodyException("BatchMail.requests must not be null or empty");
        }
        if (batchMail.getRequests().stream().anyMatch(Objects::isNull)) {
            throw new InvalidRequestBodyException("BatchMail.requests must not contain null items");
        }

    }

    private void validateContentRules(ContentView v) {
        boolean templateUuidBlank = StringUtils.isBlank(v.getTemplateUuid());

        boolean subjectTextHtmlEmpty = StringUtils.isBlank(v.getSubject())
            && StringUtils.isBlank(v.getText())
            && StringUtils.isBlank(v.getHtml());

        if (templateUuidBlank) {
            if (subjectTextHtmlEmpty) {
                throw new InvalidRequestBodyException("Mail must have subject and either text or html when templateUuid is not provided");
            }
            if (MapUtils.isNotEmpty(v.getTemplateVariables())) {
                throw new InvalidRequestBodyException("Mail templateVariables must only be used with templateUuid");
            }
            if (StringUtils.isBlank(v.getSubject())) {
                throw new InvalidRequestBodyException("Subject must not be null or empty");
            }
            if (StringUtils.isBlank(v.getText()) && StringUtils.isBlank(v.getHtml())) {
                throw new InvalidRequestBodyException("Mail must have subject and either text or html when templateUuid is not provided");
            }
        } else {
            if (StringUtils.isNotEmpty(v.getSubject())
                || StringUtils.isNotEmpty(v.getText())
                || StringUtils.isNotEmpty(v.getHtml()))
                throw new InvalidRequestBodyException("When templateUuid is used, subject, text, and html must not be used");
        }
    }

}
