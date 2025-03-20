package cn.firefox.events;

import cn.firefox.Wrapper;
import com.cubk.event.impl.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@Setter
public class EventMotion implements Event, Wrapper {
    private double x,y,z;
    private float yaw,pitch;
    private boolean onGround;
    private boolean pre;

    public EventMotion(double x, double y, double z,float yaw,float pitch,boolean onGround,boolean isPre) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.pre = isPre;
    }

    public void setPost() {
        this.pre = false;
    }

    public boolean isPost() {
        return !pre;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        mc.player.rotationYawHead = mc.player.renderYawOffset = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        mc.player.rotationPitchHead = pitch;
    }

    public void setRotations(float[] rotations) {
        this.setYaw(rotations[0]);
        this.setPitch(rotations[1]);
        mc.player.rotationYawHead = mc.player.renderYawOffset = yaw;
        mc.player.rotationPitchHead = pitch;
    }

    public void setRotations(float yaw,float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);
        mc.player.rotationYawHead = mc.player.renderYawOffset = yaw;
        mc.player.rotationPitchHead = pitch;
    }
}

