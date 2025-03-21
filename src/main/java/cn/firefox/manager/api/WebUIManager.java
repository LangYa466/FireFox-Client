package cn.firefox.manager.api;

import cn.firefox.manager.api.impl.ModuleHandler;
import cn.firefox.manager.api.impl.ToggleHandler;
import cn.firefox.manager.api.impl.UiHandler;
import cn.firefox.manager.api.impl.ValueHandler;
import cn.langya.Logger;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class WebUIManager {

    @SneakyThrows
    public void init() {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/modules", new ModuleHandler());
        server.createContext("/toggle", new ToggleHandler());
        server.createContext("/ui", new UiHandler());
        server.createContext("/setValue", new ValueHandler()); // 新增 ValueHandler
        server.setExecutor(null);
        server.start();
        Logger.info("HTTP Server started on port " + port);

        // 自动打开
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI("http://localhost:" + port + "/ui"));
        } else {
            Logger.warn("Desktop not supported. Please open the browser manually: http://localhost:" + port + "/ui");
        }
    }
}