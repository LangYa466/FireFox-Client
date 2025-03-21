package cn.firefox.ui.altmanager.misc;

import cn.firefox.Wrapper;
import cn.firefox.manager.font.CFontRenderer;
import cn.firefox.manager.font.FontManager;

public interface Screen extends Wrapper {
    // 懒得copy font
    CFontRenderer tenacityFont12 = FontManager.yahei().setSize(12),
            tenacityFont14 = FontManager.yahei().setSize(14),
            tenacityFont16 = FontManager.yahei().setSize(16),
            tenacityFont18 = FontManager.yahei().setSize(18),
            tenacityFont20 = FontManager.yahei().setSize(20),
            tenacityFont22 = FontManager.yahei().setSize(22),
            tenacityFont24 = FontManager.yahei().setSize(24),
            tenacityFont26 = FontManager.yahei().setSize(26),
            tenacityFont28 = FontManager.yahei().setSize(28),
            tenacityFont32 = FontManager.yahei().setSize(32),
            tenacityFont40 = FontManager.yahei().setSize(40),
            tenacityFont80 = FontManager.yahei().setSize(80);

    CFontRenderer tenacityBoldFont12 = tenacityFont12.bold(),
            tenacityBoldFont14 = tenacityFont14.bold(),
            tenacityBoldFont16 = tenacityFont16.bold(),
            tenacityBoldFont18 = tenacityFont18.bold(),
            tenacityBoldFont20 = tenacityFont20.bold(),
            tenacityBoldFont22 = tenacityFont22.bold(),
            tenacityBoldFont24 = tenacityFont24.bold(),
            tenacityBoldFont26 = tenacityFont26.bold(),
            tenacityBoldFont28 = tenacityFont28.bold(),
            tenacityBoldFont32 = tenacityFont32.bold(),
            tenacityBoldFont40 = tenacityFont40.bold(),
            tenacityBoldFont80 = tenacityFont80.bold();

    CFontRenderer iconFont16 = FontManager.iconFont().setSize(16),
            iconFont20 = FontManager.iconFont().setSize(20),
            iconFont26 = FontManager.iconFont().setSize(26),
            iconFont35 = FontManager.iconFont().setSize(35),
            iconFont40 = FontManager.iconFont().setSize(40);


    default void onDrag(int mouseX, int mouseY) {

    }

    void initGui();

    void keyTyped(char typedChar, int keyCode);

    void drawScreen(int mouseX, int mouseY);

    void mouseClicked(int mouseX, int mouseY, int button);

    void mouseReleased(int mouseX, int mouseY, int state);

}