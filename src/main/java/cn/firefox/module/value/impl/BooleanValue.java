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
public class BooleanValue extends Value<Boolean> {

    public BooleanValue(Boolean value) {
        super(value, null);
    }
    public BooleanValue(Boolean value, Supplier<?> supplier) {
        super(value, supplier);
    }

    public void toggle() {
        setValue(!getValue());
    }
}
