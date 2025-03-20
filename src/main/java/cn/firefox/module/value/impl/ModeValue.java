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
public class ModeValue extends Value<String> {
    private final String[] modes;

    public ModeValue(String value, String... modes) {
        super(value, null);
        this.modes = modes;
    }

    public ModeValue(String value, Supplier<?> supplier, String... modes) {
        super(value, supplier);
        this.modes = modes;
    }

    public void nextMode() {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equalsIgnoreCase(getValue())) {
                setValue(modes[(i + 1) % modes.length]);
                return;
            }
        }
    }

    public boolean isMode(String mode) {
        return getValue().equalsIgnoreCase(mode);
    }
}