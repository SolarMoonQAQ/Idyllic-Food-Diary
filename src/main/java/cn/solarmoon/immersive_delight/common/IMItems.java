package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.items.CeladonCupItem;
import cn.solarmoon.immersive_delight.common.items.FlatbreadDoughItem;
import cn.solarmoon.immersive_delight.common.items.KettleItem;
import cn.solarmoon.immersive_delight.common.items.RollingPinItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.common.IMBlocks.FLATBREAD_DOUGH_ID;
import static cn.solarmoon.immersive_delight.common.IMEntityBlocks.CELADON_CUP_ID;
import static cn.solarmoon.immersive_delight.common.IMEntityBlocks.KETTLE_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ITEMS;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMItems {

    //注册物品
    //擀面杖
    public static final RegistryObject<RollingPinItem> ROLLING_PIN = ITEMS.register("rolling_pin", RollingPinItem::new);

    //青瓷杯
    public static final RegistryObject<CeladonCupItem> CELADON_CUP = ITEMS.register(CELADON_CUP_ID, CeladonCupItem::new);
    //水壶
    public static final RegistryObject<KettleItem> KETTLE = ITEMS.register(KETTLE_ID, KettleItem::new);

    //面饼
    public static final RegistryObject<FlatbreadDoughItem> FLATBREAD_DOUGH = ITEMS.register(FLATBREAD_DOUGH_ID, FlatbreadDoughItem::new);


}
