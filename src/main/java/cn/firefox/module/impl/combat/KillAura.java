package cn.firefox.module.impl.combat;

import cn.firefox.events.EventMotion;
import cn.firefox.events.EventUpdate;
import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.module.value.impl.NumberValue;
import com.cubk.event.annotations.EventTarget;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class KillAura extends Module {
    public KillAura() {
        super("KillAura", Category.Combat);
    }

    // 测试代码 自己完善
    private final NumberValue cps = new NumberValue("CPS",10.0, 1.0, 20.0, 1.0);
    private final NumberValue range = new NumberValue("Range",4.0, 1.0, 6.0, 0.1);

    private EntityLivingBase target;
    private long lastAttackTime = 0;

    @Override
    public void onEnable() {
        target = null;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        target = mc.world.loadedEntityList.stream()
                .filter(EntityLivingBase.class::isInstance)
                .map(EntityLivingBase.class::cast)
                .filter(entity -> !entity.isDead && entity.getHealth() > 0 && entity != mc.player)
                .filter(entity -> mc.player.getDistance(entity) <= range.getValue())
                .min(Comparator.comparingDouble(entity -> mc.player.getDistance(entity)))
                .orElse(null);
    }

    @EventTarget
    public void onPreMotion(EventMotion event) {
        if (!event.isPre() || target == null) return;

        // rotation
        double diffX = target.posX - mc.player.posX;
        double diffY = (target.posY + target.getEyeHeight()) - (mc.player.posY + mc.player.getEyeHeight());
        double diffZ = target.posZ - mc.player.posZ;
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (MathHelper.atan2(diffZ, diffX) * (180.0 / Math.PI)) - 90.0F;
        float pitch = (float) -(MathHelper.atan2(diffY, dist) * (180.0 / Math.PI));

        event.setYaw(yaw);
        event.setPitch(pitch);
    }

    @EventTarget
    public void onPostMotion(EventMotion event) {
        if (!event.isPost()) return;

        long attackCooldown = (long) (1000 / cps.getValue());
        if (target != null && System.currentTimeMillis() - lastAttackTime > attackCooldown) {
            AttackOrder.sendFixedAttack(mc.player, target);
            lastAttackTime = System.currentTimeMillis();
        }
    }
}
