package cn.firefox.manager.config.impl;

import cn.firefox.manager.config.Config;
import cn.firefox.manager.FriendManager;
import cn.firefox.Client;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class FriendsConfig extends Config {

    public FriendsConfig() {
        super("Friends.json");
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        JsonArray friendArray = new JsonArray();
        FriendManager friendManager = Client.getInstance().getFriendManager();

        for (String friend : friendManager.getFriends()) {
            friendArray.add(new com.google.gson.JsonPrimitive(friend));
        }

        object.add("friends", friendArray);
        return object;
    }

    @Override
    public void loadConfig(JsonObject object) {
        if (object.has("friends") && object.get("friends").isJsonArray()) {
            JsonArray friendArray = object.getAsJsonArray("friends");
            FriendManager friendManager = Client.getInstance().getFriendManager();

            friendManager.getFriends().clear();
            for (JsonElement element : friendArray) {
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                    friendManager.add(element.getAsString());
                }
            }
        }
    }
}
