package br.com.roxcom.modustrybridge.handlers;

import br.com.roxcom.modustrybridge.services.ModsService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class InstalledModsHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        allowMethod("GET", exchange);
        authentication(exchange);

        String jsonString = ModsService.installedModsJson();

        if (jsonString == null) jsonString = "[]";

        sendJson(jsonString, exchange);
    }
}
