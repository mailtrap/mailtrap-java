package io.mailtrap.testutils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BaseTest {
    protected final Long accountId = 1L;
    protected final Long anotherAccountId = 2L;
    protected final Long inboxId = 2L;
    protected final Long projectId = 2L;
    protected final Long anotherProjectId = 2L;
    protected final Long messageId = 3L;
    protected final Long attachmentId = 4L;
    protected final Long accountAccessId = 5L;
    protected final Long sendingDomainId = 6L;
    protected final String email = "email@mailtrap.io";
    protected final String emailEncoded = URLEncoder.encode(email, StandardCharsets.UTF_8);
    protected final String contactUUID = "018dd5e3-f6d2-7c00-8f9b-e5c3f2d8a132";
    protected final String contactUUIDEncoded = URLEncoder.encode(contactUUID, StandardCharsets.UTF_8);
    protected final Long importId = 1L;
    protected final Long filterExportId = 101L;
    protected final Long exportId = 1L;
    protected final Long getFieldId = 777L;
    protected final Long updateFieldId = 999L;
    protected final Long deleteFieldId = 1111L;
    protected final String suppressionId = "2fe148b8-b019-431f-ab3f-107663fdf868";
    protected final String suppressionIdEncoded = URLEncoder.encode(suppressionId, StandardCharsets.UTF_8);
    protected final Long emailTemplateId = 2222L;
}
