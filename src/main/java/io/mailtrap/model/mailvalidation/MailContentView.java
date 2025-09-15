package io.mailtrap.model.mailvalidation;

import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.Map;

/**
 * ContentView implementation for a single-send MailtrapMail.
 */
public final class MailContentView implements ContentView {
    private final MailtrapMail mail;

    public String getSubject() {
        return mail.getSubject();
    }

    public String getText() {
        return mail.getText();
    }

    public String getHtml() {
        return mail.getHtml();
    }

    public String getTemplateUuid() {
        return mail.getTemplateUuid();
    }

    public Map<String, Object> getTemplateVariables() {
        return mail.getTemplateVariables();
    }

    public static ContentView of(MailtrapMail content) {
        return new MailContentView(content);
    }

    private MailContentView(MailtrapMail content) {
        this.mail = content;
    }
}

