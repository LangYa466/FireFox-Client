package cn.firefox.module.value.impl;

import cn.firefox.module.value.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@Setter
public class NumberValue extends Value<Double> {
    private double max, min, inc;

    public NumberValue(String name, Double value, double min, double max, double inc) {
        super(name, value, null);
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    public NumberValue(String name, Double value, double min, double max, double inc, Supplier<?> isDisplay) {
        super(name, value, isDisplay);
        this.min = min;
        this.max = max;
        this.inc = inc;
    }
}
