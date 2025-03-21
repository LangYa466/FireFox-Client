package cn.firefox.module.value.impl;

import cn.firefox.module.Module;
import cn.firefox.module.value.Value;
import cn.firefox.util.misc.HSBColor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Supplier;

@Getter
@Setter
public class ColorValue extends Value<HSBColor> {
    private Module module;

    public ColorValue(String name, Color value, Module m) {
        super(name, new HSBColor(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()),null);
        this.module = m;
        init();
    }
    public ColorValue(String name, Color value, Module m, Supplier<?> isDisplay) {
        super(name, new HSBColor(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()),isDisplay);
        this.module = m;
        init();
    }

    public Value<?> getValue(String valueName) {
        return module.getValues().stream().filter(value -> value.getName().equals(valueName)).findFirst().orElse(null);
    }

    // 优化设置值的方法
    public void setValue2(String input) {
        String[] split = input.split(":");
        if (split.length == 4) {
            this.setValue(new HSBColor(Float.parseFloat(split[0]), Float.parseFloat(split[1]),
                    Float.parseFloat(split[2]), Integer.parseInt(split[3])));
        }
    }

    public void setValue3(String input) {
        String[] split = input.split(":");
        if (split.length == 4) {
            float r = Float.parseFloat(split[0]) / 255.0f;
            float g = Float.parseFloat(split[1]) / 255.0f;
            float b = Float.parseFloat(split[2]) / 255.0f;
            int a = Integer.parseInt(split[3]);

            this.setValue(new HSBColor(r, g, b, a));
        }
    }

    private String rainbowValueName;
    private String rainbowSpeedValueName;

    private void init() {
        rainbowValueName = getName() + "Rainbow";
        rainbowSpeedValueName = getName() + "RainbowSpeed";

        module.addValue(new BooleanValue(rainbowValueName, false));
        module.addValue(new NumberValue(rainbowSpeedValueName, 3D, 10, 1, 1));
    }

    @Override
    public HSBColor getValue() {
        // 获取并处理彩虹效果
        BooleanValue rainbowValue = (BooleanValue) getValue(rainbowValueName);
        if (rainbowValue != null && rainbowValue.getValue()) {
            NumberValue rainbowSpeedValue = (NumberValue) getValue(rainbowSpeedValueName);
            if (rainbowSpeedValue != null) {
                float speed = rainbowSpeedValue.getValue().floatValue();
                float hue = System.currentTimeMillis() % (int) ((1 - speed / 15.0) * 2000);
                hue /= (int) ((1 - speed / 15.0) * 2000);

                // 设置最大饱和度和亮度
                float saturation = 1.0f;  // 最大饱和度
                float brightness = 1.0f;  // 最大亮度

                // 设置颜色
                super.getValue().setHue(hue);
                super.getValue().setSaturation(saturation);
                super.getValue().setBrightness(brightness);
            }
        }

        return super.getValue();
    }

    public Color getValueColor() {
        return getValue().getColor();
    }

    public Integer getValueRGB() {
        return getValue().getColor().getRGB();
    }
}