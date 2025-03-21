package cn.firefox.ui.mainmenu;

import cn.firefox.manager.font.FontManager;
import cn.firefox.util.render.RenderUtil;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public final class SplashScreen {
    private static final int DEFAULT_MAX = 14;
    private static int PROGRESS;
    private static String CURRENT = "";
    private static TextureManager ctm;

    static float hue2;

    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    public static void setProgress(final int givenProgress, final String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }


    public static void drawSplash(final TextureManager tm) {
        if (ctm == null) ctm = tm;

        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        final int scaleFactor = scaledresolution.getScaleFactor();

        final Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor,
                scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        Gui.drawRect(0, 0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), new Color(16, 16, 16).getRGB());

        float h_ = hue2;
        float h2 = hue2 + 85.0f;
        hue2 = (float) ((double) hue2 + 0.5);
        Color c = Color.getHSBColor(h_ / 255.0f, 0.9f, 1.0f);
        Color c2 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        int color1 = c.getRGB();
        int color2 = c2.getRGB();
        RenderUtil.drawHorizontalGradientSideways(0, 0, scaledresolution.getScaledWidth(), 2.0, color1, color2);
        drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);


        Minecraft.getMinecraft().updateDisplay();
    }

    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null)
            return;

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final float centerX = sr.getScaledWidth() / 2.0F;
        final float barWidth = 120.0F;
        final float barHeight = 8.0F;
        final float startX = centerX - (barWidth / 2.0F);
        final float endX = centerX + (barWidth / 2.0F);
        final float barY = sr.getScaledHeight() / 2.0F + 15.0F;

        final double nProgress = PROGRESS;
        final double calc = (nProgress / DEFAULT_MAX) * barWidth;

        // background
        Gui.drawRect((int) startX - 2, (int) barY - 2, (int) endX + 2, (int) (barY + barHeight) + 2, new Color(0, 0, 0, 150).getRGB());
        // progress background
        Gui.drawRect((int) startX, (int) barY, (int) endX, (int) (barY + barHeight), new Color(50, 50, 50, 200).getRGB());
        // bar
        Gui.drawRect((int) startX, (int) barY, (int) (startX + calc), (int) (barY + barHeight), new Color(255, 0, 0, 200).getRGB());

        String subtitle = "Preparing " + CURRENT + "...";
        FontManager.yahei().drawCenteredString(subtitle, centerX, barY - 10, -1);
    }
}
