package cn.firefox.module.impl.render;

import cn.firefox.Client;
import cn.firefox.events.EventRender2D;
import cn.firefox.manager.notification.Notification;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import com.cubk.event.annotations.EventTarget;

import java.util.List;

public class NotificationModule extends Module {
    public NotificationModule() {
        super("Notification", Category.Render);
    }
    
    @EventTarget
    public void onRender2D(EventRender2D event) {
        List<Notification> notificationArrayList = Client.getInstance().getNotificationManager().getNotifications();
        int pre_size = notificationArrayList.size();
        for (int j = 0; j < pre_size; j++) {
            for (int i = 0; i < notificationArrayList.size(); i++) {
                if (notificationArrayList.get(i) != null && notificationArrayList.get(i).isDone()) {
                    notificationArrayList.remove(notificationArrayList.get(i));
                    i--;
                }
            }
        }

        for (int i = 0; i < notificationArrayList.size(); i++) notificationArrayList.get(i).render(i);
    }
}
