package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.crops.BlackTeaCrop;
import cn.solarmoon.immersive_delight.common.crops.GreenTeaCrop;
import cn.solarmoon.immersive_delight.common.crops.products.BlackTeaLeaves;
import cn.solarmoon.immersive_delight.common.crops.products.GreenTeaLeaves;
import cn.solarmoon.immersive_delight.common.crops.seeds.BlackTeaSeed;
import cn.solarmoon.immersive_delight.common.crops.seeds.GreenTeaSeed;
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
    public static final RegistryObject<BlackTeaCrop> BLACK_TEA_TREE = BLOCKS.register(tree(BLACK_TEA_ID), BlackTeaCrop::new);

    //绿茶
    public static final String GREEN_TEA_ID = "green_tea";
    public static final RegistryObject<GreenTeaSeed> GREEN_TEA_SEED = ITEMS.register(seed(GREEN_TEA_ID), GreenTeaSeed::new);
    public static final RegistryObject<GreenTeaLeaves> GREEN_TEA_LEAVES = ITEMS.register(leaves(GREEN_TEA_ID), GreenTeaLeaves::new);
    public static final RegistryObject<GreenTeaCrop> GREEN_TEA_TREE = BLOCKS.register(tree(GREEN_TEA_ID), GreenTeaCrop::new);

    public static String seed(String id) {
        return id + "_seeds";
    }
    public static String leaves(String id) {
        return id + "_leaves";
    }
    public static String tree(String id) {return id + "_tree";}

}
