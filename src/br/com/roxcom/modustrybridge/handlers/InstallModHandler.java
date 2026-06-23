package br.com.roxcom.modustrybridge.handlers;

import br.com.roxcom.modustrybridge.services.ModsService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class InstallModHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        allowMethod("POST", exchange);
        authentication(exchange);
        getRequestBody(exchange);

        String id = body.getString("id");
        String version = body.getString("version", null);

        boolean success = new ModsService().installMod(id, version);

        String response = "{\"installed\": " + success + " }";
        sendJson(response, exchange);
    }
}
