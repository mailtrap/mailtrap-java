package io.mailtrap.client;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.BaseMailtrapException;
import io.mailtrap.factory.MailtrapClientFactory;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MailtrapClientTest extends BaseSendTest {

    private MailtrapClient mailtrapClient;

    @BeforeEach
    void setUp() {
        TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(Constants.EMAIL_TESTING_SEND_HOST + "/api/send/" + INBOX_ID, "POST",
                "api/emails/sendRequest.json", "api/emails/sendResponse.json"),

            DataMock.build(Constants.BULK_SENDING_HOST + "/api/send", "POST",
                "api/emails/sendRequest.json", "api/emails/sendResponse.json"),

            DataMock.build(Constants.EMAIL_SENDING_SEND_HOST + "/api/send", "POST",
                "api/emails/sendRequest.json", "api/emails/sendResponse.json"),

            DataMock.build(Constants.EMAIL_TESTING_SEND_HOST + "/api/batch/" + INBOX_ID, "POST",
                "api/emails/batchSendRequest.json", "api/emails/batchSendResponse.json"),

            DataMock.build(Constants.BULK_SENDING_HOST + "/api/batch", "POST",
                "api/emails/batchSendRequest.json", "api/emails/batchSendResponse.json"),

            DataMock.build(Constants.EMAIL_SENDING_SEND_HOST + "/api/batch", "POST",
                "api/emails/batchSendRequest.json", "api/emails/batchSendResponse.json")
        ));

        MailtrapConfig mailtrapConfig = new MailtrapConfig.Builder()
            .token("dummy-token")
            .httpClient(httpClient)
            .build();

        mailtrapClient = MailtrapClientFactory.createMailtrapClient(mailtrapConfig);
    }

    @Test
    void test_sendBulk_success() {
        // Set up test data
        MailtrapMail mailtrapMail = createValidTestMail();

        mailtrapClient.switchToBulkSendingApi();

        // Call
        SendResponse response = mailtrapClient.send(mailtrapMail);

        // Assert
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void test_sendSandbox_success() {
        // Set up test data
        MailtrapMail mailtrapMail = createValidTestMail();

        mailtrapClient.switchToEmailTestingApi(INBOX_ID);

        // Call
        SendResponse response = mailtrapClient.send(mailtrapMail);

        // Assert
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void test_send_success() {
        // Set up test data
        MailtrapMail mailtrapMail = createValidTestMail();

        // Call
        SendResponse response = mailtrapClient.send(mailtrapMail);

        // Assert
        assertEquals("11111", response.getMessageIds().get(0));
    }

    @Test
    void test_batchSendBulk_success() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createValidTestMail())).build();

        mailtrapClient.switchToBulkSendingApi();

        // Call
        BatchSendResponse response = mailtrapClient.batchSend(batchMail);

        // Assert
        assertEquals(1, response.getResponses().size());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void test_batchSendSandbox_success() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createValidTestMail())).build();

        mailtrapClient.switchToEmailTestingApi(INBOX_ID);

        // Call
        BatchSendResponse response = mailtrapClient.batchSend(batchMail);

        // Assert
        assertEquals(1, response.getResponses().size());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void test_batchSend_success() {
        // Set up test data
        MailtrapBatchMail batchMail = MailtrapBatchMail.builder().requests(List.of(createValidTestMail())).build();

        // Call
        BatchSendResponse response = mailtrapClient.batchSend(batchMail);

        // Assert
        assertEquals(1, response.getResponses().size());
        assertEquals("22222", response.getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void test_bulkAndSandboxTrue_ThrowsBaseMailtrapException() {
        // Set up test data and call
        BaseMailtrapException exception = assertThrows(BaseMailtrapException.class, () -> new MailtrapConfig.Builder()
            .bulk(true)
            .sandbox(true)
            .build());

        // Assert
        assertEquals(BULK_AND_SANDBOX_TRUE_IN_SENDING_CONFIG, exception.getMessage());
    }

    @Test
    void test_sandboxTrueAndInboxIdIsNull_ThrowsBaseMailtrapException() {
        // Set up test data and call
        BaseMailtrapException exception = assertThrows(BaseMailtrapException.class, () -> new MailtrapConfig.Builder()
            .sandbox(true)
            .inboxId(null)
            .build());

        // Assert
        assertEquals(INBOX_ID_REQUIRED, exception.getMessage());
    }

}
