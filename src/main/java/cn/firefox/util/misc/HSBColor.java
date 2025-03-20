package cn.firefox.util.misc;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter
@Getter
public class HSBColor {
    float hue, saturation, brightness;
    int alpha;

    public HSBColor(float hue, float saturation, float brightness, int alpha) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.alpha = alpha;
    }

    public HSBColor(int red, int green, int blue, int alpha) {
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = alpha;
    }

    public Color getColor() {
        return resetAlpha(Color.getHSBColor(hue, saturation, brightness), alpha);
    }

    public Color getColor(int alpha) {
        return resetAlpha(Color.getHSBColor(hue, saturation, brightness), alpha);
    }

    @Override
    public String toString() {
        return hue + ":" + saturation + ":" + brightness + ":" + alpha;
    }

    private Color resetAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
