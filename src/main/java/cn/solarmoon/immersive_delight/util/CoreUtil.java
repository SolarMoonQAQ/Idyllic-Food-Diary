package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CoreUtil {

    public static Component translation(String string1, String string2, Object... objects) {
        return Component.translatable(string1 + "." + ImmersiveDelight.MOD_ID + "." + string2, objects);
    }

    public static Component translation(String string1, String string2, ChatFormatting format, Object... objects) {
        return Component.translatable(string1 + "." + ImmersiveDelight.MOD_ID + "." + string2, objects)
                .withStyle(format);
    }

    /**
     * 自动从客户端侧发送debug
     */
    public static void deBug(String string, @Nullable Level level) {
        Minecraft mc = Minecraft.getInstance();
        if(level != null) {
            if(!level.isClientSide) return;
        }
        if(mc.player == null || !Config.deBug.get()) return;
        mc.player.sendSystemMessage(Component.literal("[§6IM§f] "  + string));
    }

    /**
     * 客户端侧debug
     */
    public static void deBug(String string) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.literal("[§6IM§f] "  + string));
        }
    }

    /**
     * 服务端测debug
     */
    public static void deBug(String string, Player player) {
        Level level = player.level();
        if(!level.isClientSide) {
            player.sendSystemMessage(Component.literal("[§6IM§f] "  + string));
        }
    }

}
