package cn.firefox.util.misc;

import cn.firefox.Wrapper;
import cn.langya.Logger;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
public class ChatUtil implements Wrapper {
    public static void log(String string) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", String.format("%sFire%sFox%s%s»» ",TextFormatting.RED,TextFormatting.WHITE,TextFormatting.BOLD,TextFormatting.YELLOW) + TextFormatting.RESET + string);

        mc.ingameGUI.addChatMessage(ChatType.CHAT, ITextComponent.Serializer.jsonToComponent(jsonObject.toString()));
        Logger.info(string);
    }
}
