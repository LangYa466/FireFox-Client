package cn.firefox.events;

import com.cubk.event.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@AllArgsConstructor
@Getter
public class EventKeyInput implements Event {
    private final int key;
}
