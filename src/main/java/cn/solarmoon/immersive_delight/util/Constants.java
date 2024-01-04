package cn.solarmoon.immersive_delight.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@OnlyIn(Dist.CLIENT)
public class Constants {

    public static Minecraft mc = Minecraft.getInstance();

    public static final Logger LOGGER = LoggerFactory.getLogger("Immersive Delight");

    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

}
