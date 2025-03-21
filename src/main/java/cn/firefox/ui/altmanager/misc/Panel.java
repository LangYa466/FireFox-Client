package cn.firefox.ui.altmanager.misc;

import cn.firefox.util.render.ColorUtil;
import cn.firefox.util.render.shader.RoundedUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Panel implements Screen {
    private float x, y, width, height;

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RoundedUtil.drawRound(x, y, width, height, 5, ColorUtil.tripleColor(27));
    }
}
