package cn.firefox.util.player;

import cn.firefox.Wrapper;
import cn.firefox.module.impl.combat.Teams;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

public class TeamUtil implements Wrapper {
    public static boolean armorTeam(EntityPlayer entityPlayer) {
        if (mc.player.inventory.armorInventory.get(3) != null && entityPlayer.inventory.armorInventory.get(3) != null) {
            ItemStack myHead = mc.player.inventory.armorInventory.get(3);
            ItemArmor myItemArmor = (ItemArmor) myHead.getItem();
            ItemStack entityHead = entityPlayer.inventory.armorInventory.get(3);
            ItemArmor entityItemArmor = (ItemArmor) entityHead.getItem();
            if (String.valueOf(entityItemArmor.getColor(entityHead)).equals("10511680")) {
                return true;
            }
            return myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead);
        }
        return false;
    }

    public static boolean colorTeam(EntityPlayer sb) {
        String targetName = StringUtils.replace(sb.getDisplayName().getFormattedText(),"§r", "");
        String clientName = mc.player.getDisplayName().getFormattedText().replace("§r", "");
        return targetName.startsWith("§" + clientName.charAt(1));
    }

    public static boolean scoreTeam(EntityPlayer entityPlayer) {
        return mc.player.isOnSameTeam(entityPlayer);
    }

    public static boolean isTeam(EntityPlayer entityPlayer) {
        if (!Teams.isEnabled) return false;
        if (Teams.color.getValue() && colorTeam(entityPlayer)) return true;
        if (Teams.armor.getValue() && armorTeam(entityPlayer)) return true;
        return Teams.scoreboard.getValue() && scoreTeam(entityPlayer);
    }
}
