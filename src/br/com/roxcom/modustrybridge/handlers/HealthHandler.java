package br.com.roxcom.modustrybridge.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HealthHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String response = "{\"status\":\"ok\"}";

        sendJson(response, exchange);
    }
}
