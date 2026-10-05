package io.mailtrap.client.api;

import io.mailtrap.api.templates.Templates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MailtrapTemplatesApi {
    private final Templates templates;
}
