package cn.firefox.module.value;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.util.function.Supplier;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@Setter
@AllArgsConstructor
public class Value<V> {
    private V value;
    private Supplier<?> isDisplay;
}
