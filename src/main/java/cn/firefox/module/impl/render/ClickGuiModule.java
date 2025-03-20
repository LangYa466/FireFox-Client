package cn.firefox.module.impl.render;

import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.ui.clickgui.ClickGUI;
import org.lwjgl.input.Keyboard;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class ClickGuiModule extends Module {
    public ClickGuiModule() {
        super("ClickGUI", Category.Render);
        setKeyCode(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) mc.displayGuiScreen(ClickGUI.INSTANCE);
        setEnabled(false);
    }
}
