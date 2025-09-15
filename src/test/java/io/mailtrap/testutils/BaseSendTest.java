package io.mailtrap.testutils;

import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.EmailAttachment;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BaseSendTest {

    protected final long INBOX_ID = 4308;
    protected final String BULK_AND_SANDBOX_TRUE_IN_SENDING_CONFIG = "Bulk mode is not applicable for Testing API";
    protected final String INBOX_ID_REQUIRED = "Testing API requires inbox ID";
    protected final String INVALID_REQUEST_EMPTY_BODY_FROM_EMAIL = "Invalid request body. Violations: from.email=must not be empty";
    protected final String TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY = "When templateUuid is used, subject, text, and html must not be used";
    protected final String TEMPLATE_VARIABLES_SHOULD_BE_USED_WITH_TEMPLATE_UUID = "Mail templateVariables must only be used with templateUuid";
    protected final String MAIL_MUST_HAVE_SUBJECT_AND_EITHER_TEXT_OR_HTML = "Mail must have subject and either text or html when templateUuid is not provided";
    protected final String MAIL_MUST_NOT_BE_NULL = "Mail must not be null";
    protected final String BATCH_MAIL_MUST_NOT_BE_NULL = "BatchMail must not be null";
    protected final String SUBJECT_MUST_NOT_BE_NULL = "Subject must not be null or empty";

    private Address getAddress(String email, String name) {
        return new Address(email, name);
    }

    private EmailAttachment getAttachment() {
        return EmailAttachment.builder()
                .filename("attachment.txt")
                .type("text/plain")
                .content("c2FtcGxlIHRleHQgaW4gdGV4dCBmaWxl")
                .build();
    }

    protected MailtrapMail createValidTestMail() {
        Address from = getAddress("sender@example.com", "John Doe");
        Address to = getAddress("receiver@example.com", "Jane Doe");
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .subject("Sample valid mail subject")
                .text("Sample valid mail text")
                .html("<html><body>Test HTML</body></html>")
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createValidTestMailForBatchWithNoSubject() {
        Address from = getAddress("sender@example.com", "John Doe");
        Address to = getAddress("receiver@example.com", "Jane Doe");
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .text("Sample valid mail text")
                .html("<html><body>Test HTML</body></html>")
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailForBatchWithNoSubjectAndText() {
        Address from = getAddress("sender@example.com", "John Doe");
        Address to = getAddress("receiver@example.com", "Jane Doe");
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createInvalidTestMail() {
        Address from = getAddress("", "John Doe");
        Address to = getAddress("receiver@example.com", null);
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .subject("Sample invalid mail subject")
                .text("Sample invalid mail text")
                .html("<html><body>Test HTML</body></html>")
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailWithoutTemplateUuidAndSubjectAndTextAndHtml() {
        Address from = getAddress("sender@example.com", "John Doe");
        Address to = getAddress("receiver@example.com", "Jane Doe");
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailWithSubjectAndNoTextAndNoHtml() {
        Address from = getAddress("sender@example.com", "John Doe");
        Address to = getAddress("receiver@example.com", "Jane Doe");
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .subject("Sample invalidvalid mail subject")
                .to(List.of(to))
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailWithTemplateUuidAndText() {
        Address from = getAddress("sender@example.com", null);
        Address to = getAddress("receiver@example.com", null);
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .subject("Sample mail with templateUuid and text")
                .text("Sample mail text")
                .templateUuid(UUID.randomUUID().toString())
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailWithTemplateUuidAndHtml() {
        Address from = getAddress("sender@example.com", "John Doe");
        Address to = getAddress("receiver@example.com", "Jane Doe");
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .subject("Sample mail with templateUuid and text")
                .html("<html><body>Test HTML</body></html>")
                .templateUuid(UUID.randomUUID().toString())
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailWithTemplateVariablesAndHtml() {
        Address from = getAddress("sender@example.com", null);
        Address to = getAddress("receiver@example.com", null);
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .subject("Sample subject")
                .html("<html><body>Test HTML</body></html>")
                .templateVariables(Map.of("user_name", "John Doe"))
                .attachments(List.of(attachment))
                .build();
    }

    protected MailtrapMail createTestMailFromTemplate() {
        Address from = getAddress("sender@example.com", null);
        Address to = getAddress("receiver@example.com", null);
        EmailAttachment attachment = getAttachment();

        return MailtrapMail.builder()
                .from(from)
                .to(List.of(to))
                .templateUuid("imagine-this-is-uuid")
                .templateVariables(Map.of("user_name", "John Doe"))
                .attachments(List.of(attachment))
                .build();
    }
}
