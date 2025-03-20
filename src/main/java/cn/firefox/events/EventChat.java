package cn.firefox.events;

import com.cubk.event.impl.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@Setter
@AllArgsConstructor
public class EventChat extends CancellableEvent {
    private final String message;
}
