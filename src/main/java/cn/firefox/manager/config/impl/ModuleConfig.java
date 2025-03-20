package cn.firefox.manager.config.impl;

import cn.firefox.module.Module;
import cn.firefox.Client;
import cn.firefox.manager.config.Config;
import cn.firefox.module.value.Value;
import cn.firefox.module.value.impl.BooleanValue;
import cn.firefox.module.value.impl.ColorValue;
import cn.firefox.module.value.impl.ModeValue;
import cn.firefox.module.value.impl.NumberValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModuleConfig extends Config {
    public ModuleConfig() {
        super("Module.json");
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();

        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            JsonObject moduleObject = new JsonObject();

            moduleObject.addProperty("state", module.isEnabled());
            moduleObject.addProperty("key", module.getKeyCode());

            JsonObject valuesObject = getJsonObject(module);

            moduleObject.add("values", valuesObject);
            object.add(module.getName(), moduleObject);
        }

        return object;
    }

    private static JsonObject getJsonObject(Module module) {
        JsonObject valuesObject = new JsonObject();

        for (Value<?> value : module.getValues()) {
            if (value instanceof NumberValue) {
                valuesObject.addProperty(value.getName(), ((NumberValue) value).getValue());
            } else if (value instanceof BooleanValue) {
                valuesObject.addProperty(value.getName(), ((BooleanValue) value).getValue());
            } else if (value instanceof ModeValue) {
                valuesObject.addProperty(value.getName(), ((ModeValue) value).getValue());
            } else if (value instanceof ColorValue) {
                valuesObject.addProperty(value.getName(), ((ColorValue) value).getValue().toString());
            }
        }
        return valuesObject;
    }

    @Override
    public void loadConfig(JsonObject object) {
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {

            if (object.has(module.getName())) {
                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();

                if (moduleObject.has("state")) module.setEnabled(moduleObject.get("state").getAsBoolean());

                if (moduleObject.has("key")) module.setKeyCode(moduleObject.get("key").getAsInt());

                if (moduleObject.has("values")) {
                    JsonObject valuesObject = moduleObject.get("values").getAsJsonObject();

                    for (Value<?> value : module.getValues()) {
                        if (valuesObject.has(value.getName())) {
                            JsonElement theValue = valuesObject.get(value.getName());
                            if (value instanceof NumberValue) {
                                ((NumberValue) value).setValue(theValue.getAsNumber().doubleValue());
                            } else if (value instanceof BooleanValue) {
                                ((BooleanValue) value).setValue(theValue.getAsBoolean());
                            } else if (value instanceof ModeValue) {
                                ((ModeValue) value).setValue(theValue.getAsString());
                            } else if (value instanceof ColorValue) {
                                ((ColorValue) value).setValue2(theValue.getAsString());
                            }
                        }
                    }
                }
            }
        }
    }
}
