package cn.firefox.util.animation.impl;

import cn.firefox.util.animation.Animation;
import cn.firefox.util.animation.Direction;
import net.minecraft.util.math.MathHelper;

public class EaseOutExpo extends Animation {
    public EaseOutExpo(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseOutExpo(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x) {
        return MathHelper.epsilonEquals((float) x, 1.0F) ? 1 : 1 - Math.pow(2, -10 * x);
    }
}
