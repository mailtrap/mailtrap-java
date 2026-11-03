package io.mailtrap;

import io.mailtrap.api.inboxes.Inboxes;
import io.mailtrap.api.sendingdomains.SendingDomains;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.client.api.MailtrapEmailTestingApi;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.MailtrapBatchMail;
import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;
import io.mailtrap.model.response.inboxes.InboxResponse;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;
import io.mailtrap.testutils.BaseSendTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import io.mailtrap.util.SendingContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The old Testing, Inbox and SendingDomain entry points still work, delegate to their
 * Sandbox and Domain replacements, and are marked {@link Deprecated}.
 */
@SuppressWarnings("deprecation")
class DeprecatedNamesTest extends BaseSendTest {

    private static final long ACCOUNT_ID = 1L;
    private static final long PROJECT_ID = 2L;
    private static final long DOMAIN_ID = 6L;
    private static final String DEPRECATED_SINCE = "1.11.0";

    private MailtrapClient client;

    @BeforeEach
    void init() {
        final TestHttpClient httpClient = new TestHttpClient(List.of(
            DataMock.build(Constants.EMAIL_SANDBOX_SEND_HOST + "/api/send/" + SANDBOX_ID, "POST",
                "api/emails/sendRequest.json", "api/emails/sendResponse.json"),
            DataMock.build(Constants.EMAIL_SANDBOX_SEND_HOST + "/api/batch/" + SANDBOX_ID, "POST",
                "api/emails/batchSendRequest.json", "api/emails/batchSendResponse.json")
        ));

        client = MailtrapClientFactory.createMailtrapClient(new MailtrapConfig.Builder()
            .httpClient(httpClient)
            .token("dummy_token")
            .build());
    }

    @Test
    void testingApi_returnsSandboxApi() {
        assertSame(client.sandboxApi(), client.testingApi());
    }

    @Test
    void inboxes_returnsSandboxes() {
        assertSame(client.sandboxApi().sandboxes(), client.sandboxApi().inboxes());
    }

    @Test
    void switchToEmailTestingApi_sendsToSandbox() {
        client.switchToEmailTestingApi(SANDBOX_ID);

        assertEquals("11111", client.send(createValidTestMail()).getMessageIds().get(0));
        assertEquals("22222", client.batchSend(MailtrapBatchMail.builder().requests(List.of(createValidTestMail())).build())
            .getResponses().get(0).getMessageIds().get(0));
    }

    @Test
    void configInboxId_setsSandboxId() {
        final MailtrapConfig config = new MailtrapConfig.Builder()
            .sandbox(true)
            .inboxId(SANDBOX_ID)
            .build();

        assertEquals(SANDBOX_ID, config.getSandboxId());
        assertEquals(SANDBOX_ID, config.getInboxId());
    }

    @Test
    void sendingContextHolderInboxId_isSandboxId() {
        final SendingContextHolder holder = SendingContextHolder.builder().inboxId(SANDBOX_ID).build();
        assertEquals(SANDBOX_ID, holder.getSandboxId());
        assertEquals(SANDBOX_ID, holder.getInboxId());

        holder.setInboxId(5L);
        assertEquals(5L, holder.getSandboxId());
    }

    @Test
    void emailTestingSendHost_isSandboxSendHost() {
        assertEquals(Constants.EMAIL_SANDBOX_SEND_HOST, Constants.EMAIL_TESTING_SEND_HOST);
    }

    @Test
    void inboxMethods_delegateToSandboxMethods() {
        final Inboxes sandboxes = mock(Inboxes.class, CALLS_REAL_METHODS);
        final CreateInboxRequest createRequest = new CreateInboxRequest(new CreateInboxRequest.InboxCreateData("Test inbox"));
        final UpdateInboxRequest updateRequest = new UpdateInboxRequest(
            new UpdateInboxRequest.InboxUpdateData("Updated Inbox Name", "updated-email-username"));
        final InboxResponse response = new InboxResponse();
        final List<InboxResponse> responses = List.of(response);

        doReturn(response).when(sandboxes).createSandbox(ACCOUNT_ID, PROJECT_ID, createRequest);
        assertSame(response, sandboxes.createInbox(ACCOUNT_ID, PROJECT_ID, createRequest));
        verify(sandboxes).createSandbox(ACCOUNT_ID, PROJECT_ID, createRequest);

        doReturn(response).when(sandboxes).getSandboxAttributes(ACCOUNT_ID, SANDBOX_ID);
        assertSame(response, sandboxes.getInboxAttributes(ACCOUNT_ID, SANDBOX_ID));
        verify(sandboxes).getSandboxAttributes(ACCOUNT_ID, SANDBOX_ID);

        doReturn(response).when(sandboxes).deleteSandbox(ACCOUNT_ID, SANDBOX_ID);
        assertSame(response, sandboxes.deleteInbox(ACCOUNT_ID, SANDBOX_ID));
        verify(sandboxes).deleteSandbox(ACCOUNT_ID, SANDBOX_ID);

        doReturn(response).when(sandboxes).updateSandbox(ACCOUNT_ID, SANDBOX_ID, updateRequest);
        assertSame(response, sandboxes.updateInbox(ACCOUNT_ID, SANDBOX_ID, updateRequest));
        verify(sandboxes).updateSandbox(ACCOUNT_ID, SANDBOX_ID, updateRequest);

        doReturn(response).when(sandboxes).cleanSandbox(ACCOUNT_ID, SANDBOX_ID);
        assertSame(response, sandboxes.cleanInbox(ACCOUNT_ID, SANDBOX_ID));
        verify(sandboxes).cleanSandbox(ACCOUNT_ID, SANDBOX_ID);

        doReturn(responses).when(sandboxes).getSandboxes(ACCOUNT_ID);
        assertSame(responses, sandboxes.getInboxes(ACCOUNT_ID));
    }

    @Test
    void sendingDomainMethods_delegateToDomainMethods() {
        final SendingDomains domains = mock(SendingDomains.class, CALLS_REAL_METHODS);
        final SendingDomainsResponse response = new SendingDomainsResponse();
        final List<SendingDomainsResponse> responses = List.of(response);
        final SendingDomainsSetupInstructionsRequest instructionsRequest = new SendingDomainsSetupInstructionsRequest("devops@test.io");

        doReturn(responses).when(domains).getDomains(ACCOUNT_ID);
        assertSame(responses, domains.getSendingDomains(ACCOUNT_ID));

        doReturn(response).when(domains).getDomain(ACCOUNT_ID, DOMAIN_ID);
        assertSame(response, domains.getSendingDomain(ACCOUNT_ID, DOMAIN_ID));
        verify(domains).getDomain(ACCOUNT_ID, DOMAIN_ID);

        domains.sendSendingDomainsSetupInstructions(ACCOUNT_ID, DOMAIN_ID, instructionsRequest);
        verify(domains).sendDomainsSetupInstructions(ACCOUNT_ID, DOMAIN_ID, instructionsRequest);

        domains.deleteSendingDomain(ACCOUNT_ID, DOMAIN_ID);
        verify(domains).deleteDomain(ACCOUNT_ID, DOMAIN_ID);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("deprecatedMembers")
    void oldNames_areMarkedDeprecated(final String name, final AnnotatedElement member) {
        final Deprecated deprecated = member.getAnnotation(Deprecated.class);

        assertNotNull(deprecated, name + " should be @Deprecated");
        assertEquals(DEPRECATED_SINCE, deprecated.since(), name);
    }

    static Stream<Arguments> deprecatedMembers() throws ReflectiveOperationException {
        return Stream.of(
            Arguments.of("MailtrapClient#testingApi", MailtrapClient.class.getMethod("testingApi")),
            Arguments.of("MailtrapClient#switchToEmailTestingApi", MailtrapClient.class.getMethod("switchToEmailTestingApi", Long.class)),
            Arguments.of("MailtrapConfig#getInboxId", MailtrapConfig.class.getMethod("getInboxId")),
            Arguments.of("MailtrapConfig.Builder#inboxId", MailtrapConfig.Builder.class.getMethod("inboxId", Long.class)),
            Arguments.of("SendingContextHolder#getInboxId", SendingContextHolder.class.getMethod("getInboxId")),
            Arguments.of("SendingContextHolder#setInboxId", SendingContextHolder.class.getMethod("setInboxId", Long.class)),
            Arguments.of("SendingContextHolderBuilder#inboxId",
                SendingContextHolder.SendingContextHolderBuilder.class.getMethod("inboxId", Long.class)),
            Arguments.of("Constants#EMAIL_TESTING_SEND_HOST", Constants.class.getField("EMAIL_TESTING_SEND_HOST")),
            Arguments.of("MailtrapEmailTestingApi#inboxes", MailtrapEmailTestingApi.class.getMethod("inboxes")),
            Arguments.of("Inboxes#createInbox", Inboxes.class.getMethod("createInbox", long.class, long.class, CreateInboxRequest.class)),
            Arguments.of("Inboxes#getInboxAttributes", Inboxes.class.getMethod("getInboxAttributes", long.class, long.class)),
            Arguments.of("Inboxes#deleteInbox", Inboxes.class.getMethod("deleteInbox", long.class, long.class)),
            Arguments.of("Inboxes#updateInbox", Inboxes.class.getMethod("updateInbox", long.class, long.class, UpdateInboxRequest.class)),
            Arguments.of("Inboxes#cleanInbox", Inboxes.class.getMethod("cleanInbox", long.class, long.class)),
            Arguments.of("Inboxes#getInboxes", Inboxes.class.getMethod("getInboxes", long.class)),
            Arguments.of("SendingDomains#getSendingDomains", SendingDomains.class.getMethod("getSendingDomains", long.class)),
            Arguments.of("SendingDomains#getSendingDomain", SendingDomains.class.getMethod("getSendingDomain", long.class, long.class)),
            Arguments.of("SendingDomains#sendSendingDomainsSetupInstructions", SendingDomains.class.getMethod(
                "sendSendingDomainsSetupInstructions", long.class, long.class, SendingDomainsSetupInstructionsRequest.class)),
            Arguments.of("SendingDomains#deleteSendingDomain", SendingDomains.class.getMethod("deleteSendingDomain", long.class, long.class))
        );
    }
}
