package cn.firefox.manager.api.impl;

import cn.firefox.Client;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class ModuleHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String categoryParam = (query != null && query.startsWith("category=")) ? query.substring(9) : "";
        String response;

        if (categoryParam.isEmpty()) {
            response = "Category parameter is required";
            exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
        } else {
            try {
                Category category = Category.valueOf(categoryParam);
                List<Module> modules = Client.getInstance().getModuleManager().getModsByCategory(category);
                response = modules.stream()
                        .map(module -> module.getName() + ": " + (module.isEnabled() ? "Enabled" : "Disabled"))
                        .collect(Collectors.joining("\n"));
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            } catch (IllegalArgumentException e) {
                response = "Invalid category: " + categoryParam;
                exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
            }
        }
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}