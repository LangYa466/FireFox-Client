package cn.firefox.manager.font;

import java.awt.*;
import java.util.HashMap;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class FontManager {
    private static final HashMap<String, CFontRenderer> fontMap = new HashMap<>();

    public static CFontRenderer getRenderer(Font font,int size) {
        String key = String.valueOf(font.getName().hashCode() + font.getStyle() + size);
        if (FontManager.fontMap.containsKey(key)) return FontManager.fontMap.get(key);
        CFontRenderer newRenderer = new CFontRenderer(font);
        FontManager.fontMap.put(key, newRenderer);
        return newRenderer;
    }

    public static CFontRenderer getRenderer(Font font) {
        return getRenderer(font,18);
    }

    private static Font getFont(String name) {
        return new Font(name, Font.PLAIN, 18);
    }

    // 我去Fonts文件夹复制的
    public static CFontRenderer yahei() { return getRenderer(getFont("Microsoft YaHei")); }
    public static CFontRenderer simsun() { return getRenderer(getFont("SimSun")); }
    public static CFontRenderer simhei() { return getRenderer(getFont("SimHei")); }
    public static CFontRenderer kaiTi() { return getRenderer(getFont("KaiTi")); }
    public static CFontRenderer youYuan() { return getRenderer(getFont("YouYuan")); }
    public static CFontRenderer fangSong() { return getRenderer(getFont("FangSong")); }
    public static CFontRenderer arial() { return getRenderer(getFont("Arial")); }
    public static CFontRenderer timesNewRoman() { return getRenderer(getFont("Times New Roman")); }
    public static CFontRenderer courierNew() { return getRenderer(getFont("Courier New")); }
    public static CFontRenderer calibri() { return getRenderer(getFont("Calibri")); }
    public static CFontRenderer cambria() { return getRenderer(getFont("Cambria")); }
    public static CFontRenderer comicSansMS() { return getRenderer(getFont("Comic Sans MS")); }
    public static CFontRenderer consolas() { return getRenderer(getFont("Consolas")); }
    public static CFontRenderer georgia() { return getRenderer(getFont("Georgia")); }
    public static CFontRenderer impact() { return getRenderer(getFont("Impact")); }
    public static CFontRenderer lucidaConsole() { return getRenderer(getFont("Lucida Console")); }
    public static CFontRenderer segoeUI() { return getRenderer(getFont("Segoe UI")); }
    public static CFontRenderer trebuchetMS() { return getRenderer(getFont("Trebuchet MS")); }
    public static CFontRenderer verdana() { return getRenderer(getFont("Verdana")); }

    public static CFontRenderer font(int size) {
        yahei().setSize(size);
        return yahei();
    }
}
