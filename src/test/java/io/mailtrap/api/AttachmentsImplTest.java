package io.mailtrap.api;

import io.mailtrap.Constants;
import io.mailtrap.api.abstractions.Attachments;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.AttachmentResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttachmentsImplTest extends BaseTest {

    private Attachments api;

    @BeforeEach
    public void init() {
        TestHttpClient httpClient = new TestHttpClient(List.of(
                DataMock.build(
                        Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/attachments",
                        "GET",
                        null,
                        "api/attachments/getAttachmentsResponse.json"
                ),
                DataMock.build(
                        Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/inboxes/" + inboxId + "/messages/" + messageId + "/attachments/" + attachmentId,
                        "GET",
                        null,
                        "api/attachments/getAttachmentResponse.json"
                )
        ));

        MailtrapConfig testConfig = new MailtrapConfig.Builder()
                .httpClient(httpClient)
                .token("dummy_token")
                .build();

        api = MailtrapClientFactory.createMailtrapClient(testConfig).testingApi().attachments();
    }

    @Test
    public void test_getAttachments() {
        List<AttachmentResponse> attachments = api.getAttachments(accountId, inboxId, messageId, null);

        assertEquals(1, attachments.size());

        AttachmentResponse attachment = attachments.get(0);
        assertEquals(attachmentId, attachment.getId());
    }

    @Test
    public void test_getAttachment() {
        AttachmentResponse attachment = api.getSingleAttachment(accountId, inboxId, messageId, attachmentId);

        assertNotNull(attachment);
        assertEquals(attachmentId, attachment.getId());
    }

}
