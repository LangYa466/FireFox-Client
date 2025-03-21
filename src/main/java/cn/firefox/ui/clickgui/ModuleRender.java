package cn.firefox.ui.clickgui;

import cn.firefox.Client;
import cn.firefox.manager.font.FontManager;
import cn.firefox.manager.notification.NotificationType;
import cn.firefox.module.Module;
import cn.firefox.module.value.Value;
import cn.firefox.module.value.impl.BooleanValue;
import cn.firefox.module.value.impl.ColorValue;
import cn.firefox.module.value.impl.ModeValue;
import cn.firefox.module.value.impl.NumberValue;
import cn.firefox.util.animation.AnimationUtils;
import cn.firefox.util.animation.Direction;
import cn.firefox.util.animation.impl.ContinualAnimation;
import cn.firefox.util.animation.impl.EaseBackIn;
import cn.firefox.util.misc.HSBColor;
import cn.firefox.util.misc.HoveringUtil;
import cn.firefox.util.misc.TimerUtil;
import cn.firefox.util.render.ColorUtil;
import cn.firefox.util.render.RenderUtil;
import cn.firefox.util.render.shader.RoundedUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import static cn.firefox.util.misc.HoveringUtil.isHovering;
import static net.minecraft.client.gui.Gui.drawRect3;

/**
 * @author xiatian233 && paimonqwq && langya466
 */
public class ModuleRender {
    public final Module m;
    private final EaseBackIn hoveranimation = new EaseBackIn(200, 0.3F, 2.0F);
    public int index;
    final ContinualAnimation settinganimation = new ContinualAnimation();
    int color;
    TimerUtil valuetimer = new TimerUtil();
    boolean cansetvalue = false;
    private int height;
    private boolean hover;
    private boolean canbind;
    private int targetAlpha = 0;
    private int nowAlpha = 0;
    private boolean openedcp;

    public ModuleRender(Module module) {
        this.m = module;
    }

    public static double round(final double value, final double inc) {
        if (inc == 0.0) return value;
        else if (inc == 1.0) return Math.round(value);
        else {
            final double halfOfInc = inc / 2.0;
            final double floored = Math.floor(value / inc) * inc;

            if (value >= floored + halfOfInc)
                return new BigDecimal(Math.ceil(value / inc) * inc)
                        .doubleValue();
            else return new BigDecimal(floored)
                    .doubleValue();
        }
    }

    public void draw(int mouseX, int mouseY, int x, int y, int moduley) {
        height = 20;
        color = m.isEnabled() ? Color.PINK.getRGB() : -1;
        hover = isHovering(x + 5, y + (float) height / 2 - (float) FontManager.font(18).getHeight() / 2 + 20 + moduley, FontManager.font(18).getStringWidth(m.getName()), FontManager.font(18).getHeight(), mouseX, mouseY);
        if (hover) {
            hoveranimation.setDirection(Direction.FORWARDS);
            canbind = true;
            targetAlpha = 255;
        } else {
            hoveranimation.setDirection(Direction.BACKWARDS);
            canbind = false;
            targetAlpha = 100;
        }
        if (cansetvalue) targetAlpha = 255;

        FontManager.font(18).drawString(m.getName(), (float) (x + (5 * (1 + hoveranimation.getOutput()))), y + height / 2 - FontManager.font(18).getHeight() / 2 + 20 + moduley, color);

        nowAlpha = (int) AnimationUtils.animate(targetAlpha, nowAlpha, 0.1);
        RoundedUtil.drawRound(x + 90, y + height / 2 - FontManager.font(18).getHeight() / 2 + 20 + moduley, 5, 5, 4f, new Color(255, 255, 255, nowAlpha));

        if (cansetvalue) {
            index = 0;
            int numberindex = 0;
            int addition = 0;
            RoundedUtil.drawRound(x, y + height / 2 - FontManager.font(18).getHeight() / 2 + 20 + moduley + FontManager.font(18).getHeight(), 100, index, 0f, new Color(0, 0, 0, 100));
            for (Value<?> value : m.getValues()) {
                if (value instanceof NumberValue numberValue) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    double d = Double.parseDouble(df.format(((((Number) numberValue.getValue()).doubleValue() - numberValue.getMin()) / (numberValue.getMax() - numberValue.getMin()))));
                    double present = 80 * d;
                    numberValue.numberAnim.animate(present, 0.1f);

                    FontManager.font(14).drawString(numberValue.getName() + " " + String.format("%.2f",numberValue.getValue()), x + 10, y + height / 2 - FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight() + 8, -1);
                    RoundedUtil.drawRound(x + 10, y + (float) height / 2 - (float) FontManager.font(18).getHeight() / 2 + 20 + moduley + FontManager.font(18).getHeight() + 8 + numberindex * 15 + addition + 10, 80, 3F, 1F, new Color(0, 0, 0, 130));
                    RoundedUtil.drawRound(x + 10, y + (float) height / 2 - (float) FontManager.font(18).getHeight() / 2 + 20 + moduley + FontManager.font(18).getHeight() + 8 + numberindex * 15 + addition + 10, (int) numberValue.numberAnim.getOutput(), 3F, 1F, Color.PINK);
                    if (isHovering(x + 10, y + (float) height / 2 - (float) FontManager.font(18).getHeight() / 2 + 20 + moduley + FontManager.font(18).getHeight() + 8 + numberindex * 15 + addition + 5, 80, 9F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                        double num = (float) Math.max(numberValue.getMin(), Math.min(numberValue.getMax(), round((mouseX - (x + 10)) * (numberValue.getMax() - numberValue.getMin()) / 80 + numberValue.getMin(), numberValue.getInc())));
                        num = (float) ((float) Math.round(num * (1.0D / numberValue.getInc())) / (1.0D / numberValue.getInc()));

                        numberValue.setValue(num);
                    }
                    numberindex++;
                    addition += 10;
                }
                index += 16;
                if (value instanceof BooleanValue booleanValue) {
                    boolean hover = isHovering(x + 5, y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight() + 8, 100, 5F, mouseX, mouseY);
                    FontManager.font(14).drawString(booleanValue.getName(), x + 10, y + height / 2 - FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight() + 8, -1);
                    if (hover && Mouse.isButtonDown(0)) {
                        if (valuetimer.hasTimeElapsed(300)) {
                            booleanValue.setValue(!booleanValue.getValue());
                            valuetimer.reset();
                        }
                    }
                    if (hover) {
                        booleanValue.easeBackIn.setDirection(Direction.FORWARDS);
                    } else {
                        booleanValue.easeBackIn.setDirection(Direction.BACKWARDS);
                    }
                    RoundedUtil.drawRound((float) (x + FontManager.font(14).getStringWidth(booleanValue.getName()) + ((15) * (1 + booleanValue.easeBackIn.getOutput()))), y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight() + 11 , 5, 5F, 1F, new Color(255, 170, 178, 130));
                    if (booleanValue.getValue()) {
                        booleanValue.decelerateAnimation.setDirection(Direction.FORWARDS);
                    } else {
                        booleanValue.decelerateAnimation.setDirection(Direction.BACKWARDS);
                    }
                    if (booleanValue.getValue())
                        FontManager.font(14).drawString("v",(float) (.5f + x + FontManager.font(14).getStringWidth(booleanValue.getName()) + ((15f) * (1 + booleanValue.easeBackIn.getOutput()))), y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 18.5f + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight() + 11, ColorUtil.applyOpacity(new Color(-1), booleanValue.decelerateAnimation.getOutput().floatValue()).getRGB());

                    numberindex++;
                }

                if (value instanceof ModeValue modeValue) {
                    FontManager.font(14).drawString(modeValue.getName(), x + 10, y + height / 2 - FontManager.font(14).getHeight() / 2 + 24 + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight(), -1);
                    RoundedUtil.drawRound(x + 10, y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition, 80, 8F, 3F, new Color(255, 170, 178, 130));
                    GlStateManager.resetColor();
                    if (isHovering(x + 10, y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition, 80, 8F, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.hasTimeElapsed(200)) {
                        if (Arrays.asList(modeValue.getModes()).indexOf(modeValue.getValue()) < modeValue.getModes().length - 1) {
                            modeValue.setValue(modeValue.getModes()[Arrays.asList(modeValue.getModes()).indexOf(modeValue.getValue()) + 1]);
                        } else {
                            modeValue.setValue(modeValue.getModes()[0]);
                        }
                        valuetimer.reset();
                    }
                    FontManager.font(14).drawString(modeValue.getValue(), x + 10 + 40 - FontManager.font(16).getStringWidth(modeValue.getValue()) / 2, y + height / 2 - FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition - 1, -1);
                    numberindex++;
                    addition += 6;
                }
                if (value instanceof ColorValue colorValue) {
                    boolean hover = isHovering(x + 10, y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition, 11, 11, mouseX, mouseY);

                    HSBColor data = colorValue.getValue();

                    final float[] hsba = {
                            data.getHue(),
                            data.getSaturation(),
                            data.getBrightness(),
                            colorValue.getValue().getAlpha(),
                    };
                    RoundedUtil.drawRound(x + 10, y + height / 2 - FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition, 11, 11, 3f, colorValue.getValueColor());
                    FontManager.font(14).drawString(colorValue.getName(), x + 10, y + (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 24 + moduley + FontManager.font(14).getHeight() + 8 + numberindex * 15 + addition - FontManager.font(14).getHeight(), 0xffffffff, false);
                    if (hover && Mouse.isButtonDown(0)) {
                        if (valuetimer.hasTimeElapsed(300)) {

                            openedcp = !openedcp;
                            valuetimer.reset();
                        }
                    }
                    if (openedcp) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(6, (float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition, 6);

                        drawRect3(x + 10 + 3, y + 8.5f + 3, 61, 61, new Color(0, 0, 0).getRGB());
                        drawRect3(x + 10, y + 8.5f + 3.5, 60, 60, getColor(Color.getHSBColor(hsba[0], 1, 1)).getRGB());
                        RenderUtil.drawHorizontalGradientSideways(x + 10, y + 8.5f + 3.5, 60, 60, getColor(Color.getHSBColor(hsba[0], 0, 1)).getRGB(), 0x00F);
                        RenderUtil.drawVerticalGradientSideways(x + 10, y + 8.5f + 3.5, 60, 60, 0x00F, getColor(Color.getHSBColor(hsba[0], 1, 0)).getRGB());

                        drawRect3(x + 10 + hsba[1] * 60 - .5, y + 8.5f + 3.5 + ((1 - hsba[2]) * 60) - .5, 1.5, 1.5, new Color(0, 0, 0).getRGB());
                        drawRect3(x + 10 + hsba[1] * 60, y + 8.5f + 3.5 + ((1 - hsba[2]) * 60), .5, .5, getColor(data.getColor()).getRGB());

                        final boolean onSB = isHovering(x + 13, y + 8.5f + 3, 61, 61, mouseX, mouseY - (height / 2 - FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition));

                        if (onSB && Mouse.isButtonDown(0)) {
                            data.setSaturation(Math.min(Math.max((mouseX - (x + 13) - 3) / 60F, 0), 1));
                            data.setBrightness(1 - Math.min(Math.max((mouseY - ((float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition) - y + 8.5f - 3 - 16) / 60F, 0), 1));
                            colorValue.setValue(data);
                        }

                        drawRect3(x + 10 + 67, y + 8.5f + 3, 10, 61, new Color(0, 0, 0).getRGB());

                        for (float f = 0F; f < 5F; f += 1F) {
                            final Color lasCol = Color.getHSBColor(f / 5F, 1F, 1F);
                            final Color tarCol = Color.getHSBColor(Math.min(f + 1F, 5F) / 5F, 1F, 1F);
                            RenderUtil.drawVerticalGradientSideways(x + 10 + 67.5, y + 8.5f + 3.5 + f * 12, 9, 12, getColor(lasCol).getRGB(), getColor(tarCol).getRGB());
                        }

                        drawRect3(x + 10 + 67.5, y + 8.5f + 2 + hsba[0] * 60, 9, 2, new Color(0, 0, 0).getRGB());
                        drawRect3(x + 10 + 67.5, y + 8.5f + 2.5 + hsba[0] * 60, 9, 1, new Color(204, 198, 255).getRGB());

                        final boolean onHue = HoveringUtil.isHovering(x + 10 + 67, y + 8.5f + 3, 10, 61, mouseX - 6, mouseY - (height / 2 - FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition));

                        if (onHue && Mouse.isButtonDown(0)) {
                            data.setHue(Math.min(Math.max((mouseY - ((float) height / 2 - (float) FontManager.font(14).getHeight() / 2 + 20 + moduley + FontManager.font(14).getHeight() + 14 + numberindex * 15 + addition) - y + 8.5f - 3 - 16) / 60F, 0), 1));
                            colorValue.setValue(data);
                        }
                        GlStateManager.popMatrix();

                    }
                    numberindex++;
                    addition += 12;
                }
            }
            index += addition;
        } else {
            for (Value<?> value : m.getValues()) {
                if (value instanceof NumberValue) {
                    value.numberAnim.setNow(0);
                }
            }
        }
    }

    private Color getColor(Color color) {
        return RenderUtil.reAlpha(color, (int) (1f * color.getAlpha()));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // 按下左键时切换模块状态
        if (hover && mouseButton == 0) {
            m.toggle();
        }
        // 按下右键时显示或隐藏设置
        if (hover && mouseButton == 1 && !m.getValues().isEmpty()) {
            cansetvalue = !cansetvalue;
        }
    }

    public void keyTyped(int keyCode) {
        if (canbind) {
            boolean ctrlPressed = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

            if (ctrlPressed && keyCode != Keyboard.KEY_LCONTROL && keyCode != Keyboard.KEY_RCONTROL) {
                Client.getInstance().getNotificationManager().post("SET KEY: " + Keyboard.getKeyName(keyCode), NotificationType.FAILED);
                m.setKeyCode(keyCode);
            }
        }
    }
}
