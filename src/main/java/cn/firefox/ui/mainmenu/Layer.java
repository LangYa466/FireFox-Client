package cn.firefox.ui.mainmenu;

import cn.firefox.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.time.Instant;

/**
 * @author : IMG
 * @create : 2024/10/26
 */
@Getter
@Setter
public class Layer {
    private final Minecraft mc;
    private ResourceLocation texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private float scale;
    private int u;
    private int v;
    private int regionWidth;
    private int regionHeight;
    private int textureWidth;
    private int textureHeight;
    private float alpha;
    private VirtualScreen virtualScreen;
    // 动画相关
    private Long duration;
    private Long startTime = null;
    private Long delay;
    private AnimationFunction<Float> xFunction;
    private AnimationFunction<Float> yFunction;
    private AnimationFunction<Float> alphaFunction;
    private AnimationFunction<Float> scaleFunction;

    public Layer(ResourceLocation texture, float x, float y, float width, float height, float scale, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, float alpha, VirtualScreen virtualScreen) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.u = u;
        this.v = v;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.alpha = alpha;
        this.virtualScreen = virtualScreen;
        this.mc = Minecraft.getMinecraft();
    }

    public void render(int mouseX, int mouseY, float delta) {
        // 设置颜色和透明度
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);

        // 绑定纹理
        mc.getTextureManager().bindTexture(texture);

        // 计算实际绘制的位置和大小
        float drawX = virtualScreen.toPracticalX(x);
        float drawY = virtualScreen.toPracticalY(y);
        float drawWidth = virtualScreen.toPracticalWidth(width * scale);
        float drawHeight = virtualScreen.toPracticalHeight(height * scale);

        RenderUtil.blit(drawX, drawY, drawWidth, drawHeight);

        tick();
    }
    public void tick() {

        if (delay == null || duration == 0) {
            return;
        }

        long currentTime = Instant.now().toEpochMilli();
        if (startTime == null) {
            startTime = currentTime;
        } else {
            float t = currentTime - startTime;
            if (t > delay) {
                float time = Math.min((float) (t - delay) / duration, 1);
                if (xFunction != null) {
                    x = xFunction.apply(time, x);
                }
                if (yFunction != null) {
                    y = yFunction.apply(time, y);
                }
                if (alphaFunction != null) {
                    alpha = alphaFunction.apply(time, alpha);
                }
                if (scaleFunction != null) {
                    scale = scaleFunction.apply(time, scale);
                }
            }
        }
    }
}
