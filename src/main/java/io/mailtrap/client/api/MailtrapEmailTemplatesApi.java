package io.mailtrap.client.api;

import io.mailtrap.api.emailtemplates.EmailTemplates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * For the paginated {@code /api/templates} endpoints, which are experimental, see
 * {@link io.mailtrap.api.templates.Templates}.
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapEmailTemplatesApi {
  private final EmailTemplates emailTemplates;
}
