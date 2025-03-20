package cn.firefox.manager.notification;

import cn.firefox.Client;
import cn.firefox.manager.element.Element;
import cn.firefox.util.animation.Easing;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class NotificationManager {
    private List<Notification> notifications;
    public final Element element = Client.getInstance().getElementManager().createElement("Notification", "Notification");

    public void init() {
        notifications = new CopyOnWriteArrayList<>();
    }

    public void post(String title,NotificationType notificationType) {
        this.notifications.add(new Notification(title, Easing.EASE_IN_OUT_QUAD,
                Easing.EASE_IN_OUT_QUAD,
                2500, notificationType));
    }
}
