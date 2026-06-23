package br.com.roxcom.modustrybridge.handlers;

import br.com.roxcom.modustrybridge.ModustryBridge;
import br.com.roxcom.modustrybridge.dialogs.RequestPairingDialog;
import br.com.roxcom.modustrybridge.services.PairingService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class PairRequestHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String pairCode = ModustryBridge.pairing.pair();

        new RequestPairingDialog(pairCode);

        String response = "{\"pairCode\":\"" + pairCode + "\"}";

        exchange.getResponseHeaders()
                .add("Content-Type", "application/json");

        exchange.sendResponseHeaders(200, response.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
