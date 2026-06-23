package br.com.roxcom.modustrybridge.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HealthHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String response = "{\"status\":\"ok\"}";

        exchange.getResponseHeaders()
                .add("Content-Type", "application/json");

        exchange.sendResponseHeaders(200, response.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
