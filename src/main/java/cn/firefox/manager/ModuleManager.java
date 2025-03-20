package cn.firefox.manager;

import cn.firefox.Client;
import cn.firefox.events.EventKeyInput;
import cn.firefox.module.Module;
import cn.firefox.module.impl.combat.*;
import cn.firefox.module.impl.move.*;
import cn.firefox.module.impl.render.*;
import com.cubk.event.annotations.EventTarget;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
public class ModuleManager {
    private final Map<String, Module> moduleMap = new HashMap<>();

    private void addModule(Module module) {
        moduleMap.put(module.getName(), module);
        // test code
        module.setEnabled(true);
    }

    private void registerModules() {
        addModule(new HUD());
        addModule(new Sprint());
        addModule(new KillAura());
    }

    public void init() {
        registerModules();
        Client.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onKeyInput(EventKeyInput event) {
        moduleMap.values().stream().filter(module -> module.getKeyCode() == event.getKey() && event.getKey() != -1).forEach(Module::toggle);
    }
}
