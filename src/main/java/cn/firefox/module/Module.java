package cn.firefox.module;

import cn.firefox.Client;
import cn.firefox.Wrapper;
import lombok.Getter;
import lombok.Setter;

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
        } else {
            onDisable();
            Client.getInstance().getEventManager().unregister(this);
        }
    }

    public void onEnable() { }
    public void onDisable() { }

    public void toggle() {
        setEnabled(!enabled);
    }
}
