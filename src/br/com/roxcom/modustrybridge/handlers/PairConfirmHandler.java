package br.com.roxcom.modustrybridge.handlers;

import arc.util.serialization.Jval;
import br.com.roxcom.modustrybridge.ModustryBridge;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class PairConfirmHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        allowMethod("POST", exchange);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Jval data = Jval.read(body);
        String pairCode = data.getString("pairCode", null);

        ModustryBridge.pairing.websiteConfirm(pairCode);

        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write("".getBytes());
        }
    }
}
