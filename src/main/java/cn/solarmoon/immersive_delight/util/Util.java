package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.init.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static cn.solarmoon.immersive_delight.util.Constants.mc;

public class Util {

    public static Component translation(String string1, String string2, Object... objects) {
        return Component.translatable(string1 + "." + ImmersiveDelight.MOD_ID + "." + string2, objects);
    }

    public static void deBug(String string, @Nullable Level level) {
        if(level != null) {
            if(!level.isClientSide) return;
        }
        if(mc.player == null || !Config.deBug.get()) return;
        mc.player.sendSystemMessage(Component.literal("[§6IM§f] "  + string));
    }

    public static String extract(String tag) {
        JsonObject jsonObject = JsonParser.parseString(tag).getAsJsonObject();
        return jsonObject.get("Potion").getAsString();
    }

}
