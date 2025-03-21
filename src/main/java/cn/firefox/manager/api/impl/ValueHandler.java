package cn.firefox.manager.api.impl;

import cn.firefox.Client;
import cn.firefox.module.Module;
import cn.firefox.module.value.Value;
import cn.firefox.module.value.impl.BooleanValue;
import cn.firefox.module.value.impl.ModeValue;
import cn.firefox.module.value.impl.NumberValue;
import cn.firefox.module.value.impl.ColorValue;
import cn.firefox.util.misc.HSBColor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class ValueHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQuery(query);
        String moduleName = params.get("module");
        String valueName = params.get("value");
        String newValue = params.get("newValue");
        String response;

        if (moduleName == null || valueName == null || newValue == null || newValue.isEmpty()) {
            response = "Missing or empty parameters";
            sendResponse(exchange, 400, response);
            return;
        }

        Module module = Client.getInstance().getModuleManager().getModule(moduleName);
        if (module == null) {
            response = "Module not found";
            sendResponse(exchange, 404, response);
            return;
        }

        Value<?> setting = module.getValues().stream()
                .filter(v -> v.getName().equalsIgnoreCase(valueName))
                .findFirst()
                .orElse(null);

        if (setting == null) {
            response = "Value not found";
            sendResponse(exchange, 404, response);
            return;
        }

        try {
            switch (setting) {
                case BooleanValue booleanValue -> booleanValue.setValue(Boolean.parseBoolean(newValue));
                case NumberValue numberValue -> numberValue.setValue(Double.parseDouble(newValue));
                case ModeValue modeValue -> modeValue.setValue(newValue);
                case ColorValue colorValue -> {
                    String[] rgba = newValue.split(",");
                    if (rgba.length != 4) {
                        throw new IllegalArgumentException("Invalid color format. Use r,g,b,a");
                    }
                    int r = Integer.parseInt(rgba[0]);
                    int g = Integer.parseInt(rgba[1]);
                    int b = Integer.parseInt(rgba[2]);
                    int a = Integer.parseInt(rgba[3]);
                    colorValue.setValue(new HSBColor(r, g, b, a));
                }
                default -> throw new IllegalArgumentException("Unsupported value type");
            }
            response = "Value updated";
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            response = "Invalid value: " + e.getMessage();
            sendResponse(exchange, 400, response);
        }
    }

    private Map<String, String> parseQuery(String query) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    params.put(key, value);
                }
            }
        }
        return params;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
