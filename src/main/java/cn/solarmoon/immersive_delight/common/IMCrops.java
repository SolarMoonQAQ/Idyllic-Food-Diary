package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.crops.BlackTeaCrop;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.BLOCKS;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMCrops {

    //红茶
    public static final String BLACK_TEA_ID = "black_tea";
    public static final RegistryObject<BlackTeaCrop> BLACK_TEA = BLOCKS.register(BLACK_TEA_ID, BlackTeaCrop::new);


}
