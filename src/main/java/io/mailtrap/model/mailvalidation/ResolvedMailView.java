package io.mailtrap.model.mailvalidation;

import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.BatchEmailBase;
import io.mailtrap.model.request.emails.EmailAttachment;
import io.mailtrap.model.request.emails.MailtrapMail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Read-only overlay that resolves the effective values (either from `base` or mail item) for batch request validation
 */
public final class ResolvedMailView {

    // may be null
    private final BatchEmailBase base;
    private final MailtrapMail item;

    public ResolvedMailView(final BatchEmailBase base, final MailtrapMail item) {
        this.base = base;
        this.item = item;
    }

    @NotNull
    @Valid
    public Address getFrom() {
        Address from = item.getFrom();

        if (from == null && base != null) {
            from = base.getFrom();
        }

        return from;
    }

    public String getSubject() {
        String subject = item.getSubject();

        if (StringUtils.isBlank(subject) && base != null) {
            subject = base.getSubject();
        }

        return subject;
    }

    public String getText() {
        String text = item.getText();

        if (StringUtils.isBlank(text) && base != null) {
            text = base.getText();
        }

        return text;
    }

    public String getHtml() {
        String html = item.getHtml();

        if (StringUtils.isBlank(html) && base != null) {
            html = base.getHtml();
        }

        return html;
    }

    public String getTemplateUuid() {
        String templateUuid = item.getTemplateUuid();

        if (StringUtils.isBlank(templateUuid) && base != null) {
            templateUuid = base.getTemplateUuid();
        }

        return templateUuid;
    }

    public Map<String, Object> getTemplateVariables() {
        Map<String, Object> templateVariables = item.getTemplateVariables();

        if ((templateVariables == null || templateVariables.isEmpty()) && base != null) {
            templateVariables = base.getTemplateVariables();
        }

        return templateVariables;
    }
}
