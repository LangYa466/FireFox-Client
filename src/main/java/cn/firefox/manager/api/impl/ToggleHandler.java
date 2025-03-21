package cn.firefox.manager.api.impl;

import cn.firefox.Client;
import cn.firefox.module.Module;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class ToggleHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String moduleName = (query != null && query.startsWith("module=")) ? query.substring(7) : "";
        String response;

        if (moduleName.isEmpty()) {
            response = "Module parameter is required";
            exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
        } else {
            Module module = Client.getInstance().getModuleManager().getModule(moduleName);
            if (module != null) {
                module.toggle();
                response = module.getName() + " is now " + (module.isEnabled() ? "Enabled" : "Disabled");
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            } else {
                response = "Module not found: " + moduleName;
                exchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);
            }
        }
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}