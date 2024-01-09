package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.crops.BlackTeaCrop;
import cn.solarmoon.immersive_delight.common.crops.products.BlackTeaLeaves;
import cn.solarmoon.immersive_delight.common.crops.seeds.BlackTeaSeed;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.BLOCKS;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ITEMS;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMCrops {

    //红茶
    public static final String BLACK_TEA_ID = "black_tea";
    public static final RegistryObject<BlackTeaSeed> BLACK_TEA_SEED = ITEMS.register(seed(BLACK_TEA_ID), BlackTeaSeed::new);
    public static final RegistryObject<BlackTeaLeaves> BLACK_TEA_LEAVES = ITEMS.register(leaves(BLACK_TEA_ID), BlackTeaLeaves::new);
    public static final RegistryObject<BlackTeaCrop> BLACK_TEA = BLOCKS.register(tree(BLACK_TEA_ID), BlackTeaCrop::new);



    public static String seed(String id) {
        return id + "_seed";
    }
    public static String leaves(String id) {
        return id + "_leaves";
    }
    public static String tree(String id) {return id + "_tree";}

}
