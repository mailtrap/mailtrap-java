package io.mailtrap.model.mailvalidation;

import java.util.Map;

/**
 * ContentView implementation for a batch item via ResolvedMailView. Exposes the already-resolved (item-first, base-fallback)
 * values to the shared content rules.
 */
public final class ResolvedMailContentView implements ContentView {
    private final ResolvedMailView mailView;

    public String getSubject() {
        return mailView.getSubject();
    }

    public String getText() {
        return mailView.getText();
    }

    public String getHtml() {
        return mailView.getHtml();
    }

    public String getTemplateUuid() {
        return mailView.getTemplateUuid();
    }

    public Map<String, Object> getTemplateVariables() {
        return mailView.getTemplateVariables();
    }

    public static ContentView of(ResolvedMailView mailView) {
        return new ResolvedMailContentView(mailView);
    }

    private ResolvedMailContentView(ResolvedMailView mailView) {
        this.mailView = mailView;
    }
}
