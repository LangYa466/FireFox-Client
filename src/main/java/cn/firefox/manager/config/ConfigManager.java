package cn.firefox.manager.config;

import cn.firefox.Wrapper;
import cn.firefox.manager.config.impl.*;
import cn.langya.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager implements Wrapper {
    public static final File dir = new File(mc.gameDir,"M0SSH3ck");
    public static final List<Config> configs = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void init() {
        if (!dir.exists()) {
            dir.mkdir();
        }
        add(new ModuleConfig());
        add(new ElementConfig());

        loadAllConfig();
    }

    public void loadConfig(String name) {
        File file = new File(dir, name);
        if (file.exists()) {
            Logger.info("Loading config: {}", name);
            for (Config config : configs) {
                if (config.getName().equals(name)) {
                    try {
                        try (FileReader reader = new FileReader(file)) {
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(reader);
                            config.loadConfig(element.getAsJsonObject());
                        }
                        break;
                    } catch (IOException e) {
                        Logger.info("Failed to load config: {}", name);
                        e.printStackTrace();
                        break;
                    }
                }
            }
        } else {
            Logger.info("Config {} doesn't exist, creating a new one...", name);
            saveConfig(name);
        }
    }

    public void saveConfig(String name) {
        File file = new File(dir, name);

        try {
            Logger.info("Saving config: {}", name);
            file.createNewFile();
            for (Config config : configs) {
                if (config.getName().equals(name)) {
                    FileUtils.writeByteArrayToFile(file, gson.toJson(config.saveConfig()).getBytes(StandardCharsets.UTF_8));
                    break;
                }
            }
        } catch (IOException e) {
            Logger.info("Failed to save config: {}", name);
        }
    }

    public void loadAllConfig() {
        Logger.info("Loading configs...");
        configs.forEach(it -> loadConfig(it.getName()));
    }

    public void saveAllConfig() {
        Logger.info("Saving configs...");
        configs.forEach(it -> saveConfig(it.getName()));
    }

    public void add(Config config) {
        configs.add(config);
    }
}
