package cn.firefox.ui.mainmenu;

import cn.firefox.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.time.Instant;
import java.util.function.Consumer;

/**
 * @author : IMG
 * @create : 2024/10/26
 */
public class TitleScreenButton {
    private static final ResourceLocation YUZU_TITLE_BUTTON_ON = new ResourceLocation("YuZuUI", "yuzu_title_button_on");
    private final ResourceLocation texture;
    private final ResourceLocation textureHover;
    private final VirtualScreen virtualScreen;
    private final Minecraft mc;
    public boolean visible = true;
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private boolean isHovered = false;
    private boolean isFocused = false;
    private Consumer<TitleScreenButton> onClick;
    /**
     * 动画相关
     */
    private Long duration;
    private Long startTime = null;
    private Long delay;
    private AnimationFunction<Float> alphaFunction;

    public TitleScreenButton(float x, float y, float width, float height, ResourceLocation texture, ResourceLocation textureHover, VirtualScreen virtualScreen, float alpha) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.textureHover = textureHover;
        this.virtualScreen = virtualScreen;
        this.alpha = alpha;
        this.mc = Minecraft.getMinecraft();
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void renderButton(int mouseX, int mouseY, float delta) {
        if (this.visible) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
            float drawX = virtualScreen.toPracticalX(x);
            float drawY = virtualScreen.toPracticalY(y);
            float drawWidth = virtualScreen.toPracticalWidth(width);
            float drawHeight = virtualScreen.toPracticalHeight(height);
            if (this.isHovered()) {
                mc.getTextureManager().bindTexture(textureHover);
            } else {
                mc.getTextureManager().bindTexture(texture);
            }
            RenderUtil.blit(drawX, drawY, drawWidth, drawHeight);
        }
    }

    public void render(int mouseX, int mouseY, float delta) {
        if (this.visible) {
            this.isHovered = this.isMouseOver(mouseX, mouseY);
            this.renderButton(mouseX, mouseY, delta);
        }

        tick();
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (this.visible && isMouseOver(mouseX, mouseY)) {
            if (onClick != null) {
                onClick.accept(this);
            }
        }
    }

    public void tick() {
        if (delay == null || duration == 0) {
            return;
        }

        long currentTime = Instant.now().toEpochMilli();
        if (startTime == null) {
            startTime = currentTime;
        } else {
            long t = currentTime - startTime;
            if (t > delay) {
                float time = Math.min((float) (t - delay) / duration, 1);
                if (alphaFunction != null) {
                    alpha = alphaFunction.apply(time, alpha);
                }
            }
        }
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        float virtualX = virtualScreen.toVirtualX((float) mouseX) - x;
        float virtualY = virtualScreen.toVirtualY((float) mouseY) - y;
        return virtualX >= 0 && virtualX < width && virtualY >= 0 && virtualY < height;
    }

    public void setOnClick(Consumer<TitleScreenButton> onClick) {
        this.onClick = onClick;
    }

    public void setAlphaFunction(AnimationFunction<Float> alphaFunction) {
        this.alphaFunction = alphaFunction;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}