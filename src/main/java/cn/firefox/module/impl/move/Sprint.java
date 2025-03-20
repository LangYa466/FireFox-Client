package cn.firefox.module.impl.move;

import cn.firefox.events.EventUpdate;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import com.cubk.event.annotations.EventTarget;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.Move);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
}
