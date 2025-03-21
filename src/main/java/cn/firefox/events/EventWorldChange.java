package cn.firefox.events;

import com.cubk.event.impl.Event;
import lombok.Getter;

import net.minecraft.world.World;

@Getter
public class EventWorldChange implements Event {

    private final World newWorld;

    public EventWorldChange(World world) {
        this.newWorld = world;
    }
}
