package cn.firefox;

import cn.firefox.manager.CommandManager;
import cn.firefox.manager.ModuleManager;
import cn.langya.Logger;
import com.cubk.event.EventManager;
import lombok.Getter;
import org.lwjgl.opengl.Display;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
public class Client {
    @Getter
    private static final Client instance = new Client();

    private final String name = "FireFox";
    private final String version = "0.1";

    private EventManager eventManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;

    private boolean initiated;

    public void init() {
        if (initiated) {
            Logger.error("Client has already been initiated!");
            return;
        }

        Display.setTitle(String.format("%s - %s",name,version));

        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        moduleManager.init();
        commandManager.init();

        initiated = true;
    }
}
