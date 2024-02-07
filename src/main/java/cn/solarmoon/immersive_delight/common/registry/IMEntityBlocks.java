package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.api.registry.core.BaseObjectRegistry;
import cn.solarmoon.immersive_delight.common.entity_block.*;
import cn.solarmoon.immersive_delight.common.entity_block.entities.*;
import cn.solarmoon.immersive_delight.util.namespace.BLOCKENTITYList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.common.registry.IMBlocks.BLOCKS;

public class IMEntityBlocks extends BaseObjectRegistry<BlockEntityType<?>> {

    //方块实体
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

    public IMEntityBlocks() {
        super(BLOCK_ENTITIES);
    }

    //青瓷杯
    public static final RegistryObject<CeladonCupEntityBlock> CELADON_CUP = BLOCKS.register(BLOCKENTITYList.CELADON_CUP, CeladonCupEntityBlock::new);
    public static final RegistryObject<BlockEntityType<CeladonCupBlockEntity>> CELADON_CUP_ENTITY = BLOCK_ENTITIES.register(BLOCKENTITYList.CELADON_CUP, () -> BlockEntityType.Builder.of(CeladonCupBlockEntity::new, CELADON_CUP.get()).build(null));


    //玉瓷杯
    public static final RegistryObject<JadeChinaCupEntityBlock> JADE_CHINA_CUP = BLOCKS.register(BLOCKENTITYList.JADE_CHINA_CUP, JadeChinaCupEntityBlock::new);
    public static final RegistryObject<BlockEntityType<JadeChinaCupBlockEntity>> JADE_CHINA_CUP_ENTITY = BLOCK_ENTITIES.register(BLOCKENTITYList.JADE_CHINA_CUP, () -> BlockEntityType.Builder.of(JadeChinaCupBlockEntity::new, JADE_CHINA_CUP.get()).build(null));

    //水壶
    public static final RegistryObject<KettleEntityBlock> KETTLE = BLOCKS.register(BLOCKENTITYList.KETTLE, KettleEntityBlock::new);
    public static final RegistryObject<BlockEntityType<KettleBlockEntity>> KETTLE_ENTITY = BLOCK_ENTITIES.register(BLOCKENTITYList.KETTLE, () -> BlockEntityType.Builder.of(KettleBlockEntity::new, KETTLE.get()).build(null));

    //汤锅
    public static final RegistryObject<SoupPotEntityBlock> SOUP_POT = BLOCKS.register(BLOCKENTITYList.SOUP_POT, SoupPotEntityBlock::new);
    public static final RegistryObject<BlockEntityType<SoupPotBlockEntity>> SOUP_POT_ENTITY = BLOCK_ENTITIES.register(BLOCKENTITYList.SOUP_POT, () -> BlockEntityType.Builder.of(SoupPotBlockEntity::new, SOUP_POT.get()).build(null));

}
