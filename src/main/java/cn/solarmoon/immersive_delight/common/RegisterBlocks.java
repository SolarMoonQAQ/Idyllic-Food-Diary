package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.blocks.CupEntityBlock;
import cn.solarmoon.immersive_delight.common.blocks.FlatbreadDoughBlock;
import cn.solarmoon.immersive_delight.common.blocks.WheatDoughBlock;
import cn.solarmoon.immersive_delight.common.blocks.entities.CupBlockEntity;
import cn.solarmoon.immersive_delight.common.blocks.renderer.FurnaceRenderer;
import cn.solarmoon.immersive_delight.common.items.CupItem;
import cn.solarmoon.immersive_delight.common.items.FlatbreadDoughItem;
import cn.solarmoon.immersive_delight.compat.farmersdelight.items.WheatDoughItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterBlocks {

    //面团
    public static final RegistryObject<WheatDoughBlock> WHEAT_DOUGH = BLOCKS.register("wheat_dough", WheatDoughBlock::new);

    //面饼
    public static final RegistryObject<FlatbreadDoughBlock> FLATBREAD_DOUGH = BLOCKS.register("flatbread_dough", FlatbreadDoughBlock::new);
    public static final RegistryObject<FlatbreadDoughItem> FLATBREAD_DOUGH_ITEM = ITEMS.register("flatbread_dough", FlatbreadDoughItem::new);

    //茶杯
    public static final RegistryObject<CupEntityBlock> CUP = BLOCKS.register("cup", CupEntityBlock::new);
    public static final RegistryObject<BlockEntityType<CupBlockEntity>> CUP_ENTITY = BLOCK_ENTITIES.register("cup", () -> BlockEntityType.Builder.of(CupBlockEntity::new, CUP.get()).build(null));
    public static final RegistryObject<CupItem> CUP_ITEM = ITEMS.register("cup", CupItem::new);

    @SubscribeEvent
    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {


    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {;
        //熔炉渲染
        event.registerBlockEntityRenderer(BlockEntityType.FURNACE, FurnaceRenderer::new);
    }

}
