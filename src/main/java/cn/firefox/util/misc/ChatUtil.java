package cn.firefox.util.misc;

import cn.firefox.Wrapper;
import cn.langya.Logger;
import net.minecraft.util.text.TextFormatting;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class ChatUtil implements Wrapper {
    public static void log(String string) {
        mc.ingameGUI.getChatGUI().addToSentMessages(String.format("%sFire%sFox%s%s»» ",TextFormatting.RED,TextFormatting.WHITE,TextFormatting.BOLD,TextFormatting.YELLOW) + TextFormatting.RESET + string);
        Logger.info(string);
    }
}
