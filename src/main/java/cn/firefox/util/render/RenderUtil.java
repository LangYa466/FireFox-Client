package cn.firefox.util.render;

import java.awt.*;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class RenderUtil {
    public static Color reAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
