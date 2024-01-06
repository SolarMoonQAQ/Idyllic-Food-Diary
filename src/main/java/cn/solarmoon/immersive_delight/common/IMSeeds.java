package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.seeds.BlackTeaSeed;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.common.IMCrops.BLACK_TEA_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ITEMS;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMSeeds {

    //红茶
    public static final RegistryObject<BlackTeaSeed> BLACK_TEA = ITEMS.register(BLACK_TEA_ID, BlackTeaSeed::new);


}
