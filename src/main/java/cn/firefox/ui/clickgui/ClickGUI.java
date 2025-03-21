package cn.firefox.ui.clickgui;

import cn.firefox.Client;
import cn.firefox.manager.font.FontManager;
import cn.firefox.module.Category;
import cn.firefox.util.misc.TimerUtil;
import cn.firefox.util.animation.Direction;
import cn.firefox.util.animation.impl.EaseBackIn;
import cn.firefox.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author xiatian233 && ChengFeng
 */
public class ClickGUI extends GuiScreen {
    private final EaseBackIn easeBackIn = new EaseBackIn(500, 1F, 1.5F);
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil startTimer = new TimerUtil();
    private final boolean got = false;
    public int indexx;
    public int indexy;
    ArrayList<CategoryPanel> categoryPanels;
    private int openIndex = 0;
    private boolean shouldDo = true;
    private boolean shouldClose = false;

    public static final ClickGUI INSTANCE = new ClickGUI();

    public ClickGUI() {
        categoryPanels = new ArrayList<>();
        for (Category category : Category.values()) {
            categoryPanels.add(new CategoryPanel(category));
        }
        easeBackIn.setDirection(Direction.BACKWARDS);
    }

    @Override
    public void initGui() {
        easeBackIn.setDirection(Direction.FORWARDS);

        timer.reset();
        startTimer.reset();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (easeBackIn.finished(Direction.BACKWARDS)) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }
        indexy = 40;
        indexx = 110;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (shouldClose && startTimer.hasTimeElapsed(categoryPanels.size() * 50L)) {
            easeBackIn.setDirection(Direction.BACKWARDS);
        }

        if (!shouldClose || startTimer.hasTimeElapsed(categoryPanels.size() * 50L)) {
            RenderUtil.scaleStart(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, easeBackIn.getOutput().floatValue());
        }
        for (CategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.drawscreen(mouseX, mouseY, indexx, indexy, (int) (120 * easeBackIn.getOutput()), easeBackIn);
            indexx += 110;
        }
        if (!shouldClose || startTimer.hasTimeElapsed(categoryPanels.size() * 50L)) {
            RenderUtil.scaleEnd();
        }

        if (startTimer.hasTimeElapsed(500)) {
            if (shouldDo && timer.hasTimeElapsed(100)) {
                categoryPanels.get(openIndex).setExtended(true);
                openIndex++;
                if (openIndex == categoryPanels.size()) {
                    openIndex = categoryPanels.size() - 1;
                    shouldDo = false;
                }
                timer.reset();
            }
        }

        // fix close
        if (shouldClose && timer.hasTimeElapsed(50)) {
            if (openIndex >= 0) {
                categoryPanels.get(openIndex).setExtended(false);
                openIndex--;
                timer.reset();
            } else {
                shouldClose = false;
            }
        }

        String text = "同时按下 Ctrl 和其他键绑定按键(鼠标移到模块)";
        FontManager.font(20).drawString(text, sr.getScaledWidth() / 2 - FontManager.font(20).getStringWidth(text) / 2, sr.getScaledHeight() - FontManager.font(20).getHeight() - 40, -1);
        String s = "ClickGUIBase @xaitian233/paimonqwq";
        FontManager.font(20).drawOutlinedString(s,width / 2 - FontManager.font(20).getStringWidth(s),1,-1, 1);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (CategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            Client.getInstance().getConfigManager().saveAllConfig();
            Minecraft.getMinecraft().displayGuiScreen(null); // 直接关闭 GUI
        }
        for (CategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.keyTyped(keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        Client.getInstance().getConfigManager().saveAllConfig();
    }
}
