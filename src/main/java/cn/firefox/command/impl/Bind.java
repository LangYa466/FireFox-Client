package cn.firefox.command.impl;

import cn.firefox.Client;
import cn.firefox.command.Command;
import cn.firefox.util.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class Bind extends Command {
    public Bind() {
        super("bind", ".bind name key");
    }

    @Override
    public boolean run(String[] args) {
        if (args.length != 3) {
            return false;
        }

        AtomicBoolean bindKey = new AtomicBoolean(false);
        String moduleName = args[1];
        Client.getInstance().getModuleManager().getModuleMap().values().stream().filter(module -> module.getName().contains(moduleName)).forEach(module -> {
            module.setKeyCode(Keyboard.getKeyIndex(args[2]));
            bindKey.set(true);
        });
        ChatUtil.log(String.format("The %s key is bound to %s!",moduleName,args[2]));
        return bindKey.get();
    }
}
