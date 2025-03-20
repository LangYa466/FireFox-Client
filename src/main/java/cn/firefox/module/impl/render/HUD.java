package cn.firefox.module.impl.render;

import cn.firefox.Client;
import cn.firefox.events.EventRender2D;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.manager.font.FontManager;
import com.cubk.event.annotations.EventTarget;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class HUD extends Module {
    public HUD() {
        super("HUD", Category.Render);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        /*
        错误写法
        int width = FontManager.yahei().setSize(18).drawStringWithShadow(Client.getInstance().getName() + " 蓝色福瑞",
                5,5,-1);
        FontManager.yahei().setSize(24).bold().drawStringWithShadow("上学记",width,5,-1);
         */
        // 正确写法
        FontManager.yahei().setSize(18);
        int width = FontManager.yahei().drawStringWithShadow(Client.getInstance().getName() + " 蓝色福瑞",
                5,5,-1);

        FontManager.yahei().bold().drawStringWithShadow("上学记",width,5,-1);
    }
}
