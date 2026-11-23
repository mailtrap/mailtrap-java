package io.mailtrap.http.impl;

import com.sun.net.httpserver.HttpServer;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.exception.http.HttpClientException;
import io.mailtrap.exception.http.HttpServerException;
import io.mailtrap.http.RequestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultMailtrapHttpClientTest {

    private HttpServer server;
    private DefaultMailtrapHttpClient client;

    @BeforeEach
    void init() throws IOException {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.start();
        client = new DefaultMailtrapHttpClient(new MailtrapConfig.Builder().token("dummy_token").build());
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void emptyClientErrorBody_throwsHttpClientException() {
        // the Subscription Topics API answers 403 with no body for an account without topics
        final String url = respond("/api/subscription_topics", 403, "");

        final HttpClientException exception = assertThrows(HttpClientException.class,
            () -> client.get(url, new RequestData(), Map.class));

        assertEquals(403, exception.getStatusCode());
        assertEquals("HTTP response code (403) received from the API server (no error info)", exception.getMessage());
    }

    @Test
    void clientErrorBody_throwsHttpClientExceptionWithItsErrors() {
        final String url = respond("/api/contacts", 422,
            "{\"errors\": {\"subscription_topics\": [\"are not available for this account\"]}}");

        final HttpClientException exception = assertThrows(HttpClientException.class,
            () -> client.get(url, new RequestData(), Map.class));

        assertEquals(422, exception.getStatusCode());
        assertEquals("are not available for this account", exception.getMessage());
    }

    @Test
    void serverErrorBody_appendsItsErrorsToTheMessage() {
        final String error = "The contact was saved, but its subscription topic preferences were not. "
            + "Send them again in an update of contact 018dd5e3-f6d2-7c00-8f9b-e5c3f2d8a132.";
        final String url = respond("/api/contacts", 503, "{\"errors\": \"" + error + "\"}");

        final HttpServerException exception = assertThrows(HttpServerException.class,
            () -> client.get(url, new RequestData(), Map.class));

        assertEquals(503, exception.getStatusCode());
        assertEquals("Internal Server Error. HTTP response code (503) received from the API server. "
            + "Retry later or contact support. " + error, exception.getMessage());
    }

    @Test
    void serverErrorWithoutJsonBody_keepsTheGenericMessage() {
        final String url = respond("/api/contacts", 502, "<html><body>Bad Gateway</body></html>");

        final HttpServerException exception = assertThrows(HttpServerException.class,
            () -> client.get(url, new RequestData(), Map.class));

        assertEquals(502, exception.getStatusCode());
        assertEquals("Internal Server Error. HTTP response code (502) received from the API server. "
            + "Retry later or contact support.", exception.getMessage());
    }

    @Test
    void nullErrorBody_keepsTheGenericMessage() {
        final String url = respond("/api/contacts", 503, "null");

        final HttpServerException exception = assertThrows(HttpServerException.class,
            () -> client.get(url, new RequestData(), Map.class));

        assertEquals(503, exception.getStatusCode());
        assertEquals("Internal Server Error. HTTP response code (503) received from the API server. "
            + "Retry later or contact support.", exception.getMessage());
    }

    private String respond(final String path, final int statusCode, final String body) {
        server.createContext(path, exchange -> {
            final byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, bytes.length == 0 ? -1 : bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        });

        return "http://" + server.getAddress().getHostString() + ":" + server.getAddress().getPort() + path;
    }
}
