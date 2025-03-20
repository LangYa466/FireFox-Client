package cn.firefox.events;

import com.cubk.event.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@AllArgsConstructor
public class EventRender2D implements Event {
    private final float partialTicks;
    private final ScaledResolution scaledResolution;
}
