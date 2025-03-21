package cn.firefox.manager;

import cn.firefox.Client;
import cn.firefox.events.EventKeyInput;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.module.impl.combat.*;
import cn.firefox.module.impl.move.*;
import cn.firefox.module.impl.render.*;
import cn.firefox.module.value.Value;
import cn.langya.Logger;
import com.cubk.event.annotations.EventTarget;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
public class ModuleManager {
    private final Map<String, Module> moduleMap = new HashMap<>();

    private void addModule(Module module) {
        moduleMap.put(module.getName(), module);

        for (final Field field : module.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object obj = field.get(module);
                if (obj instanceof Value<?>) module.getValues().add((Value<?>) obj);
            } catch (IllegalAccessException e) {
                Logger.error(e.getMessage());
            }
        }

        // test code
        module.setEnabled(true);
    }

    public void registerModules() {
        addModule(new HUD());
        addModule(new Sprint());
        addModule(new KillAura());
        addModule(new NotificationModule());
        addModule(new ClickGuiModule());
        addModule(new Teams());
    }

    public void init() {
        Client.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onKeyInput(EventKeyInput event) {
        moduleMap.values().stream().filter(module -> module.getKeyCode() == event.getKey() && event.getKey() != -1).forEach(Module::toggle);
    }

    public List<Module> getModsByCategory(Category m) {
        return moduleMap.values().stream()
                .filter(module -> module.getCategory() == m)
                .collect(Collectors.toList());
    }

    public Module getModule(Class<?> moduleClass) {
        for (Module module : moduleMap.values()) {
            if (moduleClass == module.getClass()) return module;
        }
        return null;
    }

    public Module getModule(String name) {
        for (Module module : moduleMap.values()) {
            if (module.getName().toLowerCase().contains(name.toLowerCase())) return module;
        }
        return null;
    }
}
