package br.com.roxcom.modustrybridge.handlers;

import arc.util.serialization.Jval;
import br.com.roxcom.modustrybridge.ModustryBridge;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

abstract public class BaseHandler implements HttpHandler {
    Jval body;

    protected void addCorsHeaders(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();

        headers.add("Access-Control-Allow-Origin", "https://modustry.com.br");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, X-Modustry-Pair-Code");

        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
    }

    protected void allowMethod(String method, HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals(method)) {
            byte[] error = "{\"error\": \"  Method now allowed\"}".getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders()
                    .set("Content-Type", "application/json; charset=utf-8");

            exchange.sendResponseHeaders(405, error.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(error);
            }

            return;
        }
    }

    protected void authentication(HttpExchange exchange) throws IOException {
        String pairCode = exchange.getRequestHeaders().getFirst("X-Modustry-Pair-Code");
        if (!ModustryBridge.pairing.verify(pairCode)) {
            ModustryBridge.pairing.unpair();

            byte[] error = "{\"error\": \"Unauthorized\"}".getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders()
                    .set("Content-Type", "application/json; charset=utf-8");

            exchange.sendResponseHeaders(401, error.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(error);
            }

            return;
        }
    }

    protected void getRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        this.body = Jval.read(body);
    }

    protected void sendJson(String response, HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders()
                .add("Content-Type", "application/json");

        exchange.sendResponseHeaders(200, response.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
