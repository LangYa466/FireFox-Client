package cn.firefox.command.impl;

import cn.firefox.Client;
import cn.firefox.command.Command;
import cn.firefox.util.misc.ChatUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class Toggle extends Command {
    public Toggle() {
        super("t", ".t name");
    }

    @Override
    public boolean run(String[] args) {
        if (args.length != 2) {
            return false;
        }

        AtomicBoolean toggle = new AtomicBoolean(false);
        String moduleName = args[1];
        Client.getInstance().getModuleManager().getModuleMap().values().stream().filter(module -> module.getName().contains(moduleName)).forEach(module -> {
            module.toggle();
            toggle.set(true);
        });

        ChatUtil.log(String.format("Toggled %s!",moduleName));
        return toggle.get();
    }
}
