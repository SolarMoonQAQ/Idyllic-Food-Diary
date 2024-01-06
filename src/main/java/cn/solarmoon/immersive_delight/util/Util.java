package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.init.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

import static cn.solarmoon.immersive_delight.util.Constants.mc;

public class Util {

    public static Component translation(String string1, String string2, Object... objects) {
        return Component.translatable(string1 + "." + ImmersiveDelight.MOD_ID + "." + string2, objects);
    }

    /**
     * 自动从客户端侧发送debug
     */
    public static void deBug(String string, @Nullable Level level) {
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

    /**
     * 提取tag中的词条
     * @param tag 要提取的tag
     * @param extractTag 指定一个词条
     * @return 返回词条对应的条目
     */
    public static String extract(String tag, String extractTag) {
        JsonObject jsonObject = JsonParser.parseString(tag).getAsJsonObject();
        return jsonObject.get(extractTag).getAsString();
    }

    public static boolean isLoad(String modId) {
        return ModList.get().isLoaded(modId);
    }

}
