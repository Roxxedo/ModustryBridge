package br.com.roxcom.modustrybridge.handlers;

import br.com.roxcom.modustrybridge.ModustryBridge;
import br.com.roxcom.modustrybridge.dialogs.RequestPairingDialog;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PairRequestHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        allowMethod("GET", exchange);

        ModustryBridge.pairing.unpair();
        String pairCode = ModustryBridge.pairing.pair();

        new RequestPairingDialog(pairCode);

        String response = "{\"pairCode\":\"" + pairCode + "\"}";

        sendJson(response, exchange);
    }
}
