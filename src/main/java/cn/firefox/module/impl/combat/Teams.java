package cn.firefox.module.impl.combat;

import cn.firefox.module.Category;
import cn.firefox.module.Module;
import cn.firefox.module.value.impl.BooleanValue;

public class Teams extends Module {
    public static boolean isEnabled;

    public Teams() {
        super("Team", Category.Combat);
    }

    public static final BooleanValue color = new BooleanValue("Color",true);
    public static final BooleanValue armor = new BooleanValue("Armor",true);
    public static final BooleanValue scoreboard = new BooleanValue("ScoreBoard",true);

    @Override
    public void onEnable() {
        isEnabled = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        isEnabled = false;
        super.onDisable();
    }
}
