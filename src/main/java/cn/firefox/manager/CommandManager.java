package cn.firefox.manager;

import cn.firefox.Client;
import cn.firefox.command.Command;
import cn.firefox.command.impl.*;
import cn.firefox.events.EventChat;
import cn.firefox.util.misc.ChatUtil;
import com.cubk.event.annotations.EventTarget;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
public class CommandManager {
    private final Map<String, Command> commandMap = new HashMap<>();
    private final String prefix = ".";

    public void addCommand(Command command) {
        commandMap.put(command.getName(), command);
    }

    public void registerCommands() {
        addCommand(new Bind());
        addCommand(new Toggle());
    }

    public void init() {
        registerCommands();
        Client.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onChat(EventChat event) {
        String message = event.getMessage();
        String[] args = message.split(" ");
        if (message.startsWith(".")) {
            Optional<Command> optionalCommand = commandMap.values().stream()
                    .filter(command -> Objects.equals(command.getName(), args[0].substring(1))) // substring删除.
                    .findFirst();

            if (optionalCommand.isPresent()) {
                Command command = optionalCommand.get();
                event.setCancelled(true);
                if (!command.run(args)) ChatUtil.log(String.format("[CommandManager] %s", command.getUsage()));
            } else {
                ChatUtil.log("[CommandManager] the command was not found");
            }
        }
    }
}
