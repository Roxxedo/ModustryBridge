package br.com.roxcom.modustrybridge.handlers;

import br.com.roxcom.modustrybridge.ModustryBridge;
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

        String status = ModustryBridge.pairing.isPaired() ? "paired" : "running";

        String response = "{ \"status\": \"" + status + "\" }";

        sendJson(response, exchange);
    }
}
