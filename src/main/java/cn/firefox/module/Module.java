package cn.firefox.module;

import cn.firefox.Client;
import cn.firefox.Wrapper;
import cn.firefox.manager.notification.NotificationType;
import cn.firefox.module.value.Value;
import cn.firefox.module.value.impl.BooleanValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@Setter
public class Module implements Wrapper {
    private final String name;
    private final Category category;
    private String suffix;
    private int keyCode;

    private List<Value<?>> values = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Module(String name) {
        this.name = name;
        this.category = Category.Misc;
    }

    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
            Client.getInstance().getEventManager().register(this);
            if (mc.player != null)
                Client.getInstance().getNotificationManager().post(name + " Enabled", NotificationType.SUCCESS);
        } else {
            onDisable();
            Client.getInstance().getEventManager().unregister(this);
            if (mc.player != null)
                Client.getInstance().getNotificationManager().post(name + " Disabled", NotificationType.FAILED);
        }
    }

    public void onEnable() { }
    public void onDisable() { }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void addValue(Value<?> value) {
        values.add(value);
    }
}
