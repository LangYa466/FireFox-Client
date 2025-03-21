package cn.firefox.manager;

import cn.firefox.Client;
import cn.firefox.manager.notification.NotificationType;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class FriendManager {
    private final ArrayList<String> friends;

    public FriendManager() {
        friends = new ArrayList<>();
    }

    public void add(String name) {
        if (!friends.contains(name)) {
            friends.add(name);
            Client.getInstance().getNotificationManager().post("Added friend: " + name, NotificationType.SUCCESS);
            Client.getInstance().getConfigManager().saveConfig("Friends.json");
        } else {
            Client.getInstance().getNotificationManager().post(name + " is already your friend!",NotificationType.FAILED);
        }
    }

    public void remove(String name) {
        if (friends.contains(name)) {
            friends.remove(name);
            Client.getInstance().getNotificationManager().post("Removed friend: " + name,NotificationType.SUCCESS);
            Client.getInstance().getConfigManager().saveConfig("Friends.json");

        } else {
            Client.getInstance().getNotificationManager().post("Friend not found: " + name,NotificationType.FAILED);
        }
    }

    public boolean isFriend(String name) {
        return friends.contains(name);
    }

}
