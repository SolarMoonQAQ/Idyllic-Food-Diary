package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.blocks.FlatbreadDoughBlock;
import cn.solarmoon.immersive_delight.common.blocks.WheatDoughBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMBlocks {

    //面团
    public static final String WHEAT_DOUGH_ID = "wheat_dough";
    public static final RegistryObject<WheatDoughBlock> WHEAT_DOUGH = BLOCKS.register(WHEAT_DOUGH_ID, WheatDoughBlock::new);

    //面饼
    public static final String FLATBREAD_DOUGH_ID = "flatbread_dough";
    public static final RegistryObject<FlatbreadDoughBlock> FLATBREAD_DOUGH = BLOCKS.register(FLATBREAD_DOUGH_ID, FlatbreadDoughBlock::new);

}
