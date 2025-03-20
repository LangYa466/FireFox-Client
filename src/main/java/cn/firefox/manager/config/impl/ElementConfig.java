package cn.firefox.manager.config.impl;

import cn.firefox.Client;
import cn.firefox.manager.config.Config;
import cn.firefox.manager.element.Element;
import com.google.gson.JsonObject;

public class ElementConfig extends Config {
    public ElementConfig() {super("Hud.json");}

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        for (Element element : Client.getInstance().getElementManager().getElementMap().values()) {
            JsonObject elementObject = new JsonObject();

            elementObject.addProperty("x",element.getX());
            elementObject.addProperty("y",element.getY());

            object.add(element.getElementName(),elementObject);
        }
        return object;
    }

    @Override
    public void loadConfig(JsonObject object) {
        for (Element element : Client.getInstance().getElementManager().getElementMap().values()) {
            if (object.has(element.getElementName())) {
                JsonObject elementObject = object.get(element.getElementName()).getAsJsonObject();
                if (elementObject.has("x")) element.setX(elementObject.get("x").getAsFloat());

                if (elementObject.has("y")) element.setY(elementObject.get("y").getAsFloat());
            }
        }
    }
}
