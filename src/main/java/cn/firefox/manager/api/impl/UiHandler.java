package cn.firefox.manager.api.impl;

import cn.firefox.Client;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.module.value.Value;
import cn.firefox.module.value.impl.BooleanValue;
import cn.firefox.module.value.impl.ColorValue;
import cn.firefox.module.value.impl.ModeValue;
import cn.firefox.module.value.impl.NumberValue;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class UiHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String modulesHtml = buildModulesHtml();
        byte[] response = getResponse(modulesHtml);
        sendResponse(exchange, response);
    }

    private String buildModulesHtml() {
        StringBuilder html = new StringBuilder();
        for (Category category : Category.values()) {
            html.append("<div class='text-lg font-bold text-purple-400 mb-2'>")
                    .append(category.name())
                    .append("</div>")
                    .append("<div class='bg-gray-800 rounded-lg shadow-md mb-3'>");
            for (Module module : Client.getInstance().getModuleManager().getModsByCategory(category)) {
                html.append(buildModuleHtml(module));
            }
            html.append("</div>");
        }
        return html.toString();
    }

    private String buildModuleHtml(Module module) {
        String name = module.getName();
        boolean enabled = module.isEnabled();
        return "<div class='p-3 border-b border-gray-700 last:border-0'>" +
                "<div class='flex items-center justify-between'>" +
                "<span class='" + (enabled ? "text-green-400" : "text-red-400") + "'>" + name + "</span>" +
                "<button onclick='toggleModule(\"" + name + "\")' class='px-4 py-1 text-black bg-purple-400 hover:bg-purple-500 rounded-lg transition'>Toggle</button>" +
                "</div>" +
                buildSettingsHtml(module) +
                "</div>";
    }

    private String buildSettingsHtml(Module module) {
        StringBuilder settings = new StringBuilder("<div class='mt-2 space-y-2'>");
        for (Value<?> value : module.getValues()) {
            settings.append(generateSettingHtml(module.getName(), value));
        }
        settings.append("</div>");
        return settings.toString();
    }

    private String generateSettingHtml(String moduleName, Value<?> setting) {
        String name = setting.getName();
        String id = moduleName + "_" + name;

        // java 21 moment
        return switch (setting) {
            case BooleanValue bool -> formatBooleanSetting(id, name, bool.getValue(), moduleName);
            case NumberValue num -> formatNumberSetting(id, name, num, moduleName);
            case ModeValue mode -> formatModeSetting(id, name, mode, moduleName);
            case ColorValue color -> formatColorSetting(id, name, color.getValueRGB(), moduleName);
            default -> "<div>Error...</div>";
        };
    }

    private String formatBooleanSetting(String id, String name, boolean value, String moduleName) {
        return String.format(
                "<div class='flex items-center justify-between'>" +
                        "<label for='%s' class='text-gray-300'>%s</label>" +
                        "<input type='checkbox' id='%s' %s onchange='setValue(\"%s\", \"%s\", this.checked ? \"true\" : \"false\")'>" +
                        "</div>",
                id, name, id, value ? "checked" : "", moduleName, name
        );
    }

    private String formatNumberSetting(String id, String name, NumberValue num, String moduleName) {
        return String.format(
                "<div class='flex items-center justify-between'>" +
                        "<label for='%s' class='text-gray-300'>%s</label>" +
                        "<input type='number' id='%s' value='%s' min='%s' max='%s' step='%s' onchange='setValue(\"%s\", \"%s\", this.value)' " +
                        "class='bg-gray-700 text-white border border-gray-600 rounded px-2 py-1 w-24'>" +
                        "</div>",
                id, name, id, num.getValue(), num.getMin(), num.getMax(), num.getInc(), moduleName, name
        );
    }

    private String formatModeSetting(String id, String name, ModeValue mode, String moduleName) {
        String options = Arrays.stream(mode.getModes())
                .map(m -> String.format("<option value='%s' %s>%s</option>", m, m.equals(mode.getValue()) ? "selected" : "", m))
                .collect(Collectors.joining());
        return String.format(
                "<div class='flex items-center justify-between'>" +
                        "<label for='%s' class='text-gray-300'>%s</label>" +
                        "<select id='%s' onchange='setValue(\"%s\", \"%s\", this.value)' class='bg-gray-700 text-white border border-gray-600 rounded px-2 py-1'>" +
                        "%s</select>" +
                        "</div>",
                id, name, id, moduleName, name, options
        );
    }

    private String formatColorSetting(String id, String name, int rgb, String moduleName) {
        String hexColor = String.format("#%06X", (rgb & 0xFFFFFF));
        return String.format(
                "<div class='flex items-center justify-between'>" +
                        "<label for='%s' class='text-gray-300'>%s</label>" +
                        "<input type='color' id='%s' value='%s' class='w-10 h-10 border-0 rounded cursor-pointer bg-transparent' " +
                        "onchange='setValue(\"%s\", \"%s\", this.value)'>" +
                        "</div>",
                id, name, id, hexColor, moduleName, name
        );
    }


    private byte[] getResponse(String modulesHtml) {
        String html = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>FireFoxLite</title>
            <script src="https://cdn.tailwindcss.com"></script>
            <script>
                async function toggleModule(name) {
                    await fetch("/toggle?module=" + encodeURIComponent(name));
                    location.reload();
                }
                async function setValue(module, value, newValue) {
                    if (newValue.startsWith("#")) {
                        const r = parseInt(newValue.substring(1, 3), 16);
                        const g = parseInt(newValue.substring(3, 5), 16);
                        const b = parseInt(newValue.substring(5, 7), 16);
                        newValue = `${r},${g},${b},255`;
                    }
                    await fetch(`/setValue?module=${encodeURIComponent(module)}&value=${encodeURIComponent(value)}&newValue=${encodeURIComponent(newValue)}`);
                    location.reload();
                }
            </script>
        </head>
        <body class="bg-gray-900 text-white font-sans p-6">
            <div class="max-w-3xl mx-auto">
                <h1 class="text-4xl font-bold text-center text-red-400 mb-6">FireFox</h1>
                <div class="space-y-4">{{MODULES}}</div>
            </div>
        </body>
        </html>
        """;
        html = html.replace("{{MODULES}}", modulesHtml);
        return html.getBytes(StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, byte[] response) throws IOException {
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}