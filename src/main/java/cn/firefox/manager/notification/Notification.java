package cn.firefox.manager.notification;

import cn.firefox.Client;
import cn.firefox.Wrapper;
import cn.firefox.manager.element.Element;
import cn.firefox.manager.font.FontManager;
import cn.firefox.util.animation.Easing;
import cn.firefox.util.animation.EasingAnimation;
import cn.firefox.util.render.shader.RoundedUtil;
import cn.firefox.util.render.shader.ShaderElement;
import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

/**
 * @author yuxiangll, LangYa
 * @version 1.1
 */
@Getter
public class Notification implements Wrapper {
    private final String content;
    private final EasingAnimation animationX, animationY, animationProcess;
    private final NotificationType type;
    private final Color color;
    private final long begin_time, duration;
    private final float height = 40;

    public Notification(String content, Easing easingX, Easing easingY, long duration, NotificationType type) {
        this.content = content;
        this.animationX = new EasingAnimation(easingX, (long) (duration * 0.2), 0);
        this.animationY = new EasingAnimation(easingY, (long) (duration * 0.2), 0);
        this.animationProcess = new EasingAnimation(Easing.EASE_OUT_QUART, (long) (duration * 0.8), 0);
        this.type = type;
        this.color = type.getColor();
        begin_time = System.currentTimeMillis();
        this.duration = duration;
    }

    public boolean isDone() {
        return System.currentTimeMillis() >= begin_time + duration;
    }

    public void render(int index) {
        Element element = Client.getInstance().getNotificationManager().element;
        FontRenderer font = FontManager.font(18);
        float width = font.getStringWidth(content) + 30;
        float height = 20;
        element.setWH(width, height);

        float x = element.getX();
        float y = element.getY() + -(index * height * 1.25f);

        // 背景
        RoundedUtil.drawGradientRound(x, y, width, height, 5,
                color.darker(),color.darker(), new Color(0, 0, 0, 150),new Color(0, 0, 0, 150));

        // 阴影
        ShaderElement.addBlurTask(() -> RoundedUtil.drawRound(x, y, width, height, 12, new Color(0, 0, 0, 80)));

        // 进度条效果
        RoundedUtil.drawGradientRound(x, y, width * (float) animationProcess.getValue(1), height, 5,
                color.darker(),color.darker(), new Color(0, 0, 0, 150),new Color(0, 0, 0, 150));

        font.drawString(content, (int) (x + 10), (int) (y + (height - font.FONT_HEIGHT) / 2f), Color.WHITE.getRGB());
    }
}
