package io.mailtrap.client.api;

import io.mailtrap.api.emailtemplates.EmailTemplates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @deprecated use {@link io.mailtrap.api.templates.Templates}
 */
@Deprecated
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapEmailTemplatesApi {
  private final EmailTemplates emailTemplates;
}
