package cn.firefox;

import cn.firefox.manager.CommandManager;
import cn.firefox.manager.FriendManager;
import cn.firefox.manager.ModuleManager;
import cn.firefox.manager.api.WebUIManager;
import cn.firefox.manager.config.ConfigManager;
import cn.firefox.manager.element.ElementManager;
import cn.firefox.manager.notification.NotificationManager;
import cn.firefox.ui.altmanager.GuiAltManager;
import cn.langya.Logger;
import com.cubk.event.EventManager;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.ViaMCP;
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
    private ElementManager elementManager;
    private ConfigManager configManager;
    private NotificationManager notificationManager;
    private FriendManager friendManager;
    private WebUIManager webUIManager;
    private GuiAltManager altManager;

    private boolean initiated;
    public static boolean updateGuiScale;
    public static int prevGuiScale;

    public void init() {
        if (initiated) {
            Logger.error("Client has already been initiated!");
            return;
        }

        Display.setTitle(String.format("%s - %s",name,version));

        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        elementManager = new ElementManager();
        configManager = new ConfigManager();
        notificationManager = new NotificationManager();
        friendManager = new FriendManager();
        webUIManager = new WebUIManager();
        altManager = new GuiAltManager();

        moduleManager.init();
        commandManager.init();
        configManager.init();
        notificationManager.init();
        webUIManager.init();

        initViaMCP();

        initiated = true;
    }

    private void initViaMCP() {
        try {
            ViaMCP.create();

            ViaMCP.INSTANCE.initAsyncSlider();
            ViaMCP.INSTANCE.initAsyncSlider(5, 5, 110,20);
            ViaLoadingBase.getInstance().reload(ProtocolVersion.v1_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
