package io.mailtrap;

import io.mailtrap.api.inboxes.Inboxes;
import io.mailtrap.api.sendingdomains.SendingDomains;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.client.api.MailtrapEmailTestingApi;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.inboxes.CreateInboxRequest;
import io.mailtrap.model.request.inboxes.UpdateInboxRequest;
import io.mailtrap.model.request.sendingdomains.SendingDomainsSetupInstructionsRequest;
import io.mailtrap.model.response.inboxes.InboxResponse;
import io.mailtrap.model.response.sendingdomains.SendingDomainsResponse;
import io.mailtrap.testutils.BaseTest;
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
class DeprecatedNamesTest extends BaseTest {

    private static final String DEPRECATED_SINCE = "1.11.0";

    private MailtrapClient client;

    @BeforeEach
    void init() {
        client = MailtrapClientFactory.createMailtrapClient(new MailtrapConfig.Builder()
            .httpClient(new TestHttpClient(List.of()))
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
    void switchToEmailTestingApi_switchesToSandboxApi() {
        final MailtrapClient spiedClient = spy(client);

        spiedClient.switchToEmailTestingApi(sandboxId);

        verify(spiedClient).switchToEmailSandboxApi(sandboxId);
    }

    @Test
    void configInboxId_setsSandboxId() {
        final MailtrapConfig config = new MailtrapConfig.Builder()
            .sandbox(true)
            .inboxId(sandboxId)
            .build();

        assertEquals(sandboxId, config.getSandboxId());
        assertEquals(sandboxId, config.getInboxId());
    }

    @Test
    void sendingContextHolderInboxId_isSandboxId() {
        final SendingContextHolder holder = SendingContextHolder.builder().inboxId(sandboxId).build();
        assertEquals(sandboxId, holder.getSandboxId());
        assertEquals(sandboxId, holder.getInboxId());

        holder.setInboxId(5L);
        assertEquals(5L, holder.getSandboxId());
    }

    @Test
    void emailTestingSendHost_isSandboxSendHost() {
        assertEquals(Constants.EMAIL_SANDBOX_SEND_HOST, Constants.EMAIL_TESTING_SEND_HOST);
    }

    // Each new method is stubbed with the exact arguments, and the mock answers anything else
    // with a default value, so assertSame alone shows the old method delegated to the new one.
    @Test
    void inboxMethods_delegateToSandboxMethods() {
        final Inboxes sandboxes = mock(Inboxes.class, CALLS_REAL_METHODS);
        final CreateInboxRequest createRequest = new CreateInboxRequest(new CreateInboxRequest.InboxCreateData("Test inbox"));
        final UpdateInboxRequest updateRequest = new UpdateInboxRequest(
            new UpdateInboxRequest.InboxUpdateData("Updated Inbox Name", "updated-email-username"));
        final InboxResponse response = new InboxResponse();
        final List<InboxResponse> responses = List.of(response);

        doReturn(response).when(sandboxes).createSandbox(accountId, projectId, createRequest);
        assertSame(response, sandboxes.createInbox(accountId, projectId, createRequest));

        doReturn(response).when(sandboxes).getSandboxAttributes(accountId, sandboxId);
        assertSame(response, sandboxes.getInboxAttributes(accountId, sandboxId));

        doReturn(response).when(sandboxes).deleteSandbox(accountId, sandboxId);
        assertSame(response, sandboxes.deleteInbox(accountId, sandboxId));

        doReturn(response).when(sandboxes).updateSandbox(accountId, sandboxId, updateRequest);
        assertSame(response, sandboxes.updateInbox(accountId, sandboxId, updateRequest));

        doReturn(response).when(sandboxes).cleanSandbox(accountId, sandboxId);
        assertSame(response, sandboxes.cleanInbox(accountId, sandboxId));

        doReturn(responses).when(sandboxes).getSandboxes(accountId);
        assertSame(responses, sandboxes.getInboxes(accountId));
    }

    @Test
    void sendingDomainMethods_delegateToDomainMethods() {
        final SendingDomains domains = mock(SendingDomains.class, CALLS_REAL_METHODS);
        final SendingDomainsResponse response = new SendingDomainsResponse();
        final List<SendingDomainsResponse> responses = List.of(response);
        final SendingDomainsSetupInstructionsRequest instructionsRequest = new SendingDomainsSetupInstructionsRequest("devops@test.io");

        doReturn(responses).when(domains).getDomains(accountId);
        assertSame(responses, domains.getSendingDomains(accountId));

        doReturn(response).when(domains).getDomain(accountId, domainId);
        assertSame(response, domains.getSendingDomain(accountId, domainId));

        domains.sendSendingDomainsSetupInstructions(accountId, domainId, instructionsRequest);
        verify(domains).sendDomainsSetupInstructions(accountId, domainId, instructionsRequest);

        domains.deleteSendingDomain(accountId, domainId);
        verify(domains).deleteDomain(accountId, domainId);
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
            deprecatedMethod(MailtrapClient.class, "testingApi"),
            deprecatedMethod(MailtrapClient.class, "switchToEmailTestingApi", Long.class),
            deprecatedMethod(MailtrapConfig.class, "getInboxId"),
            deprecatedMethod(MailtrapConfig.Builder.class, "inboxId", Long.class),
            deprecatedMethod(SendingContextHolder.class, "getInboxId"),
            deprecatedMethod(SendingContextHolder.class, "setInboxId", Long.class),
            deprecatedMethod(SendingContextHolder.SendingContextHolderBuilder.class, "inboxId", Long.class),
            Arguments.of("Constants#EMAIL_TESTING_SEND_HOST", Constants.class.getField("EMAIL_TESTING_SEND_HOST")),
            deprecatedMethod(MailtrapEmailTestingApi.class, "inboxes"),
            deprecatedMethod(Inboxes.class, "createInbox", long.class, long.class, CreateInboxRequest.class),
            deprecatedMethod(Inboxes.class, "getInboxAttributes", long.class, long.class),
            deprecatedMethod(Inboxes.class, "deleteInbox", long.class, long.class),
            deprecatedMethod(Inboxes.class, "updateInbox", long.class, long.class, UpdateInboxRequest.class),
            deprecatedMethod(Inboxes.class, "cleanInbox", long.class, long.class),
            deprecatedMethod(Inboxes.class, "getInboxes", long.class),
            deprecatedMethod(SendingDomains.class, "getSendingDomains", long.class),
            deprecatedMethod(SendingDomains.class, "getSendingDomain", long.class, long.class),
            deprecatedMethod(SendingDomains.class, "sendSendingDomainsSetupInstructions",
                long.class, long.class, SendingDomainsSetupInstructionsRequest.class),
            deprecatedMethod(SendingDomains.class, "deleteSendingDomain", long.class, long.class)
        );
    }

    private static Arguments deprecatedMethod(final Class<?> type, final String name, final Class<?>... parameterTypes)
        throws NoSuchMethodException {
        final String typeName = type.getCanonicalName().substring(type.getPackageName().length() + 1);

        return Arguments.of(typeName + "#" + name, type.getMethod(name, parameterTypes));
    }
}
