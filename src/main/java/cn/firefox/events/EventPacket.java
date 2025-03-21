package cn.firefox.events;

import com.cubk.event.impl.CancellableEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

@Getter
@Setter
public class EventPacket extends CancellableEvent {
    public Packet<?> packet;
    public EventPacket(Packet<?> packet) {
        this.packet = packet;
    }
    public static class Send extends EventPacket {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends EventPacket {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
