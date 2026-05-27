package io.mailtrap.examples.webhooks;

import com.sun.net.httpserver.HttpServer;
import io.mailtrap.webhooks.WebhookSignatures;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebhookSignatureExample {

    public static void main(final String[] args) throws IOException {
        final String signingSecret = System.getenv("MAILTRAP_WEBHOOK_SIGNING_SECRET");

        final HttpServer server = HttpServer.create(new InetSocketAddress(9292), 0);
        server.createContext("/webhooks/mailtrap", exchange -> {
            // Use the raw request body — parsing and re-serializing the JSON may
            // reorder keys or alter whitespace and invalidate the signature.
            final String payload;
            try (InputStream body = exchange.getRequestBody()) {
                payload = new String(body.readAllBytes(), StandardCharsets.UTF_8);
            }
            final String signature = exchange.getRequestHeaders().getFirst("Mailtrap-Signature");

            if (!WebhookSignatures.verify(payload, signature, signingSecret)) {
                exchange.sendResponseHeaders(401, -1);
                exchange.close();
                return;
            }

            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });
        server.start();
    }
}
