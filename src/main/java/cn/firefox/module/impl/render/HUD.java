package cn.firefox.module.impl.render;

import cn.firefox.Client;
import cn.firefox.events.EventRender2D;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.manager.font.FontManager;
import cn.firefox.module.value.impl.BooleanValue;
import cn.firefox.module.value.impl.NumberValue;
import cn.firefox.util.render.shader.KawaseBloom;
import cn.firefox.util.render.shader.KawaseBlur;
import cn.firefox.util.render.shader.ShaderElement;
import com.cubk.event.annotations.EventTarget;
import net.minecraft.client.shader.Framebuffer;

import static cn.firefox.util.render.shader.ShaderElement.createFrameBuffer;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class HUD extends Module {
    public HUD() {
        super("HUD", Category.Render);
    }

    // blur
    public BooleanValue blur = new BooleanValue("Blur", false);
    public final NumberValue iterations = new NumberValue("BlurIterations", 2D, 1, 8, 1, () -> blur.getValue());
    public final NumberValue offset = new NumberValue("BlurOffset", 3D, 1, 10, 1, () -> blur.getValue());
    // shadow
    public BooleanValue bloom = new BooleanValue("Bloom (Shadow)", false);
    public final NumberValue shadowRadius = new NumberValue("BloomIterations", 3D, 1, 8, 1, () -> bloom.getValue());
    public final NumberValue shadowOffset = new NumberValue("BloomOffset", 1D, 1, 10, 1, () -> bloom.getValue());
    private Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);
    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

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


    public void drawBlur() {
        stencilFramebuffer = createFrameBuffer(stencilFramebuffer);

        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(false);

        for (Runnable runnable : ShaderElement.getTasks()) {
            runnable.run();
        }
        ShaderElement.getTasks().clear();

        stencilFramebuffer.unbindFramebuffer();

        KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, iterations.getValue().intValue(), offset.getValue().intValue());
    }

    public void drawBloom() {

        bloomFramebuffer = createFrameBuffer(bloomFramebuffer);
        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(false);

        for (Runnable runnable : ShaderElement.getBloomTasks()) {
            runnable.run();
        }
        ShaderElement.getBloomTasks().clear();

        bloomFramebuffer.unbindFramebuffer();

        KawaseBloom.renderBlur(bloomFramebuffer.framebufferTexture, shadowRadius.getValue().intValue(), shadowOffset.getValue().intValue());
    }
}
