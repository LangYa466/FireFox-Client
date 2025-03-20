package cn.firefox.ui.mainmenu;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : IMG, LangYa
 * @create : 2024/10/26
 */
@Setter
@Getter
public class VirtualScreen {
    // 虚拟宽高
    private int virtualWidth;
    private int virtualHeight;

    // 实际宽高
    private int practicalWidth;
    private int practicalHeight;

    private int currentX;
    private int currentY;

    public VirtualScreen() {
    }

    public VirtualScreen(int virtualWidth, int virtualHeight) {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
    }

    public float toPracticalX(float x) {
        return currentX + x * (float) practicalWidth / (float) virtualWidth;
    }

    public float toPracticalY(float y) {
        return currentY + y * (float) practicalHeight / (float) virtualHeight;
    }

    public float toPracticalWidth(float width) {
        return width * (float) practicalWidth / (float) virtualWidth;
    }

    public float toPracticalHeight(float height) {
        return height * (float) practicalHeight / (float) virtualHeight;
    }

    public float toVirtualX(float x) {
        return (x - currentX) * (float) virtualWidth / (float) practicalWidth;
    }

    public float toVirtualY(float y) {
        return (y - currentY) * (float) virtualHeight / (float) practicalHeight;
    }

}
