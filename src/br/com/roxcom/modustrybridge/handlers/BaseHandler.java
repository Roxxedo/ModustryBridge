package br.com.roxcom.modustrybridge.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

abstract public class BaseHandler implements HttpHandler {
    protected void addCorsHeaders(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();

        headers.add("Access-Control-Allow-Origin", "https://modustry.com");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, X-Modustry-Pair-Code");
    }
}
