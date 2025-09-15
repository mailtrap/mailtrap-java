package io.mailtrap.api.testingemails;

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

class TestingEmailsImplTest extends BaseSendTest {

    private TestingEmails testingApi;

    @BeforeEach
    public void init() {
        TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(
                Constants.EMAIL_TESTING_SEND_HOST + "/api/send/" + INBOX_ID,
                "POST", "api/emails/sendRequest.json", "api/emails/sendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_TESTING_SEND_HOST + "/api/send/" + INBOX_ID,
                "POST", "api/emails/sendRequestFromTemplate.json", "api/emails/sendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_TESTING_SEND_HOST + "/api/batch/" + INBOX_ID,
                "POST", "api/emails/batchSendRequest.json", "api/emails/batchSendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_TESTING_SEND_HOST + "/api/batch/" + INBOX_ID,
                "POST", "api/emails/batchSendWithBaseSubjectRequest.json", "api/emails/batchSendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_TESTING_SEND_HOST + "/api/batch/" + INBOX_ID,
                "POST", "api/emails/batchSendWithBaseSubjectAndTextRequest.json", "api/emails/batchSendResponse.json"
            ),
            DataMock.build(
                Constants.EMAIL_TESTING_SEND_HOST + "/api/batch/" + INBOX_ID,
                "POST", "api/emails/batchSendRequestFromTemplate.json", "api/emails/batchSendResponse.json"
            )
        ));

        MailtrapConfig testConfig = new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .sandbox(true)
            .inboxId(INBOX_ID)
            .build();

        testingApi = MailtrapClientFactory.createMailtrapClient(testConfig).testingApi().emails();
    }

    @Test
    void send_InvalidMailEmptyFromEmail_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapMail mail = createInvalidTestMail();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(mail, INBOX_ID));
        assertEquals(INVALID_REQUEST_EMPTY_BODY_FROM_EMAIL, exception.getMessage());
    }

    @Test
    void send_MailWithoutTemplateUuidAndTextAndHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapMail mail = createTestMailWithoutTemplateUuidAndSubjectAndTextAndHtml();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(mail, INBOX_ID));
        assertEquals(MAIL_MUST_HAVE_SUBJECT_AND_EITHER_TEXT_OR_HTML, exception.getMessage());
    }

    @Test
    void send_MailWithTemplateUuidAndText_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapMail mail = createTestMailWithTemplateUuidAndText();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(mail, INBOX_ID));
        assertEquals(TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY, exception.getMessage());
    }

    @Test
    void send_MailWithTemplateUuidAndHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapMail mail = createTestMailWithTemplateUuidAndHtml();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(mail, INBOX_ID));
        assertEquals(TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY, exception.getMessage());
    }

    @Test
    void send_MailWithTemplateVariablesAndHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapMail mail = createTestMailWithTemplateVariablesAndHtml();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(mail, INBOX_ID));
        assertEquals(TEMPLATE_VARIABLES_SHOULD_BE_USED_WITH_TEMPLATE_UUID, exception.getMessage());
    }

    @Test
    void send_MailWithSubjectAndNoTextNoHtml_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapMail mail = createTestMailWithSubjectAndNoTextAndNoHtml();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(mail, INBOX_ID));
        assertEquals(MAIL_MUST_HAVE_SUBJECT_AND_EITHER_TEXT_OR_HTML, exception.getMessage());
    }

    @Test
    void send_NullableMail_ThrowsInvalidRequestBodyException() {
        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.send(null, INBOX_ID));
        assertEquals(MAIL_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void send_ValidMail_SuccessResponse() {
        // Set up test data
        var mail = createValidTestMail();

        // Perform call
        SendResponse response = testingApi.send(mail, INBOX_ID);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void send_ValidMailFromTemplate_SuccessResponse() {
        // Set up test data
        var mail = createTestMailFromTemplate();

        // Perform call
        SendResponse response = testingApi.send(mail, INBOX_ID);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMail_SuccessResponse() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createValidTestMail())).build();

        // Perform call
        BatchSendResponse response = testingApi.batchSend(batchMail, INBOX_ID);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMailFromTemplate_SuccessResponse() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createTestMailFromTemplate())).build();

        // Perform call
        BatchSendResponse response = testingApi.batchSend(batchMail, INBOX_ID);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMailWithSubjectFromBase_SuccessResponse() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder()
            .base(BatchEmailBase.builder().subject("Sample valid mail subject").build())
            .requests(List.of(createValidTestMailForBatchWithNoSubject())).build();

        // Perform call
        BatchSendResponse response = testingApi.batchSend(batchMail, INBOX_ID);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_ValidMailWithSubjectAndTextFromBase_SuccessResponse() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder()
            .base(BatchEmailBase.builder().subject("Sample valid mail subject").text("Sample valid mail text").build())
            .requests(List.of(createTestMailForBatchWithNoSubjectAndText())).build();

        // Perform call
        BatchSendResponse response = testingApi.batchSend(batchMail, INBOX_ID);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void batchSend_InvalidMailWithNoSubjectAndTextNoBase_ThrowsInvalidRequestBodyException() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder()
            .base(BatchEmailBase.builder().text("Sample valid mail text").build())
            .requests(List.of(createTestMailForBatchWithNoSubjectAndText())).build();

        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.batchSend(batchMail, INBOX_ID));
        assertEquals(SUBJECT_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void batchSend_NullableMail_ThrowsInvalidRequestBodyException() {
        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.batchSend(null, INBOX_ID));
        assertEquals(BATCH_MAIL_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void batchSend_MailWithTemplateUuidAndText_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createTestMailWithTemplateUuidAndText())).build();


        // Assert
        InvalidRequestBodyException exception = assertThrows(InvalidRequestBodyException.class, () -> testingApi.batchSend(batchMail, INBOX_ID));
        assertEquals(TEMPLATE_UUID_IS_USED_SUBJECT_AND_TEXT_AND_HTML_SHOULD_BE_EMPTY, exception.getMessage());
    }

}
