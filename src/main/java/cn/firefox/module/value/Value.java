package cn.firefox.module.value;

import cn.firefox.util.animation.impl.DecelerateAnimation;
import cn.firefox.util.animation.impl.EaseBackIn;
import cn.firefox.util.animation.impl.OutputAnimation;
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
    private String name;
    private V value;
    private Supplier<?> isDisplay;

    public final OutputAnimation numberAnim = new OutputAnimation(0);
    public final EaseBackIn easeBackIn = new EaseBackIn(200, 0.2F, 1.0F);
    public final DecelerateAnimation decelerateAnimation = new DecelerateAnimation(200, 1F);
}
