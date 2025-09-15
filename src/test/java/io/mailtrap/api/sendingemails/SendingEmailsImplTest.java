package io.mailtrap.api.sendingemails;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.InvalidRequestBodyException;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.BatchEmailBase;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.BatchSendResponse;
import io.mailtrap.model.response.emails.SendResponse;
import io.mailtrap.testutils.BaseSendTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendingEmailsImplTest extends BaseSendTest {

    private SendingEmails sendApi;

    @BeforeEach
    public void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(
                Constants.EMAIL_SENDING_SEND_HOST + "/api/send",
                "POST", "api/emails/sendRequest.json", "api/emails/sendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_SENDING_SEND_HOST + "/api/send",
                "POST", "api/emails/sendRequestFromTemplate.json", "api/emails/sendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_SENDING_SEND_HOST + "/api/batch",
                "POST", "api/emails/batchSendRequest.json", "api/emails/batchSendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_SENDING_SEND_HOST + "/api/batch",
                "POST", "api/emails/batchSendWithBaseSubjectRequest.json", "api/emails/batchSendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_SENDING_SEND_HOST + "/api/batch",
                "POST", "api/emails/batchSendWithBaseSubjectAndTextRequest.json", "api/emails/batchSendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_SENDING_SEND_HOST + "/api/batch",
                "POST", "api/emails/batchSendRequestFromTemplate.json", "api/emails/batchSendResponse.json"
            )
        ));

        final MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build();

        sendApi = MailtrapClientFactory.createMailtrapClient(testConfig).sendingApi().emails();
    }

    @Test
    void send_InvalidMailEmptyFromEmail_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapMail mail = createInvalidTestMail();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(mail));
        assertEquals(INVALID_REQUEST_EMPTY_BODY_FROM_EMAIL, exception.getMessage());
    }

    @Test
    void send_MailWithoutTemplateUuidAndTextAndHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapMail mail = createTestMailWithoutTemplateUuidAndSubjectAndTextAndHtml();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(mail));
        assertEquals(MAIL_MUST_HAVE_SUBJECT_AND_EITHER_TEXT_OR_HTML, exception.getMessage());
    }

    @Test
    void send_MailWithTemplateUuidAndText_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapMail mail = createTestMailWithTemplateUuidAndText();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(mail));
        assertEquals(TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY, exception.getMessage());
    }

    @Test
    void send_MailWithTemplateUuidAndHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapMail mail = createTestMailWithTemplateUuidAndHtml();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(mail));
        assertEquals(TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY, exception.getMessage());
    }

    @Test
    void send_MailWithTemplateVariablesAndHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapMail mail = createTestMailWithTemplateVariablesAndHtml();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(mail));
        assertEquals(TEMPLATE_VARIABLES_SHOULD_BE_USED_WITH_TEMPLATE_UUID, exception.getMessage());
    }

    @Test
    void send_MailWithSubjectAndNoTextNoHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapMail mail = createTestMailWithSubjectAndNoTextAndNoHtml();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(mail));
        assertEquals(MAIL_MUST_HAVE_SUBJECT_AND_EITHER_TEXT_OR_HTML, exception.getMessage());
    }

    @Test
    void send_NullableMail_ThrowsInvalidRequestBodyException() {
        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.send(null));
        assertEquals(MAIL_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void send_ValidMail_SuccessResponse() {
        // Set up test data
        final var mail = createValidTestMail();

        // Perform call
        final SendResponse response = sendApi.send(mail);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void send_ValidMailFromTemplate_SuccessResponse() {
        // Set up test data
        final var mail = createTestMailFromTemplate();

        // Perform call
        final SendResponse response = sendApi.send(mail);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMail_SuccessResponse() {
        // Set up test data
        final MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createValidTestMail())).build();

        // Perform call
        final BatchSendResponse response = sendApi.batchSend(batchMail);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMailFromTemplate_SuccessResponse() {
        // Set up test data
        final MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createTestMailFromTemplate())).build();

        // Perform call
        final BatchSendResponse response = sendApi.batchSend(batchMail);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMailWithSubjectFromBase_SuccessResponse() {
        // Set up test data
        final MailtrapBatchMail batchMail = MailtrapBatchMail.builder()
            .base(BatchEmailBase.builder().subject("Sample valid mail subject").build())
            .requests(List.of(createValidTestMailForBatchWithNoSubject())).build();

        // Perform call
        final BatchSendResponse response = sendApi.batchSend(batchMail);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMailWithSubjectAndTextFromBase_SuccessResponse() {
        // Set up test data
        final MailtrapBatchMail batchMail = MailtrapBatchMail.builder()
            .base(BatchEmailBase.builder().subject("Sample valid mail subject").text("Sample valid mail text").build())
            .requests(List.of(createTestMailForBatchWithNoSubjectAndText())).build();

        // Perform call
        final BatchSendResponse response = sendApi.batchSend(batchMail);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_InvalidMailWithNoSubjectAndTextNoBase_ThrowsInvalidRequestBodyException() {
        // Set up test data
        final MailtrapBatchMail batchMail = MailtrapBatchMail.builder()
            .base(BatchEmailBase.builder().text("Sample valid mail text").build())
            .requests(List.of(createTestMailForBatchWithNoSubjectAndText())).build();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.batchSend(batchMail));
        assertEquals(SUBJECT_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void batchSend_NullableMail_ThrowsInvalidRequestBodyException() {
        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.batchSend(null));
        assertEquals(BATCH_MAIL_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void batchSend_MailWithTemplateUuidAndText_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        final MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createTestMailWithTemplateUuidAndText())).build();

        // Assert
        final InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> sendApi.batchSend(batchMail));
        assertEquals(TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY, exception.getMessage());
    }
}
