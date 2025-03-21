package cn.firefox.module.impl.misc;

import java.text.DecimalFormat;

import cn.firefox.events.EventPacket;
import cn.firefox.events.EventWorldChange;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.module.value.impl.BooleanValue;
import cn.firefox.util.misc.ChatUtil;
import cn.firefox.util.player.TeamUtil;
import com.cubk.event.annotations.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.TextFormatting;

public class HackerDetector extends Module {
    public BooleanValue reachValue = new BooleanValue("Reach", true);
    public static final DecimalFormat DF_1 = new DecimalFormat("0.000000");
    int vl;

    public HackerDetector() {
        super("HackerDetector", Category.Misc);
    }

    @Override
    public void onEnable() {
        this.vl = 0;
    }

    @EventTarget
    public void onWorld(EventWorldChange event) {
        this.vl = 0;
    }

    @EventTarget
    public void onPacket(EventPacket.Receive event) {
        if (mc.player == null) return;
        if (mc.player.ticksExisted % 6 == 0) {
            SPacketEntityStatus s19;
            if (event.getPacket() instanceof SPacketEntityStatus && this.reachValue.getValue() && (s19 = (SPacketEntityStatus)event.getPacket()).getOpCode() == 2) {
                new Thread(() -> this.checkCombatHurt(s19.getEntity(mc.world))).start();
            }
        }
    }

    private void checkCombatHurt(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) {
            return;
        }
        Entity attacker = null;
        int attackerCount = 0;
        for (Entity worldEntity : mc.world.getLoadedEntityList()) {
            if (!(worldEntity instanceof EntityPlayer) || worldEntity.getDistance(entity) > 7.0f || worldEntity.equals(entity)) continue;
            ++attackerCount;
            attacker = worldEntity;
        }
        if (attacker == null || attacker.equals(entity) || TeamUtil.isTeam((EntityPlayer) attacker)) {
            return;
        }
        double reach = attacker.getDistance(entity);
        String prefix = TextFormatting.GRAY + "[" + TextFormatting.AQUA + "HackerDetector" + TextFormatting.GRAY + "] " + TextFormatting.RESET + TextFormatting.GRAY + attacker.getName() + TextFormatting.WHITE + " failed ";
        if (reach > 3.0) {
            ChatUtil.log(prefix + TextFormatting.AQUA + "Reach" + TextFormatting.WHITE + " (vl:" + attackerCount + ".0)" + TextFormatting.GRAY + ": " + DF_1.format(reach) + " blocks");
        }
    }
}