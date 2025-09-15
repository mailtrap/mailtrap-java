package io.mailtrap.model.mailvalidation;

import java.util.Map;

/**
 * An adapter used by mail content validation rules. It exposes only the fields that participate in
 * subject/text/html/template logic so the same rules can run for both single and batch sends.
 */
public interface ContentView {

    String getSubject();

    String getText();

    String getHtml();

    String getTemplateUuid();

    Map<String, Object> getTemplateVariables();

}
