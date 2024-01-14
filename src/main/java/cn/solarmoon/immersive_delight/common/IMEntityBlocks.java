package cn.solarmoon.immersive_delight.common;

import cn.solarmoon.immersive_delight.common.entity_blocks.CeladonCupEntityBlock;
import cn.solarmoon.immersive_delight.common.entity_blocks.JadeChinaCupEntityBlock;
import cn.solarmoon.immersive_delight.common.entity_blocks.KettleEntityBlock;
import cn.solarmoon.immersive_delight.common.entity_blocks.SoupPotEntityBlock;
import cn.solarmoon.immersive_delight.common.entity_blocks.entities.CeladonCupBlockEntity;
import cn.solarmoon.immersive_delight.common.entity_blocks.entities.JadeChinaCupBlockEntity;
import cn.solarmoon.immersive_delight.common.entity_blocks.entities.KettleBlockEntity;
import cn.solarmoon.immersive_delight.common.entity_blocks.entities.SoupPotBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.BLOCKS;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.BLOCK_ENTITIES;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMEntityBlocks {

    //青瓷杯
    public static final String CELADON_CUP_ID = "celadon_cup";
    public static final RegistryObject<CeladonCupEntityBlock> CELADON_CUP = BLOCKS.register(CELADON_CUP_ID, CeladonCupEntityBlock::new);
    public static final RegistryObject<BlockEntityType<CeladonCupBlockEntity>> CELADON_CUP_ENTITY = BLOCK_ENTITIES.register(CELADON_CUP_ID, () -> BlockEntityType.Builder.of(CeladonCupBlockEntity::new, CELADON_CUP.get()).build(null));

    //玉瓷杯
    public static final String JADE_CHINA_CUP_ID = "jade_china_cup";
    public static final RegistryObject<JadeChinaCupEntityBlock> JADE_CHINA_CUP = BLOCKS.register(JADE_CHINA_CUP_ID, JadeChinaCupEntityBlock::new);
    public static final RegistryObject<BlockEntityType<JadeChinaCupBlockEntity>> JADE_CHINA_CUP_ENTITY = BLOCK_ENTITIES.register(JADE_CHINA_CUP_ID, () -> BlockEntityType.Builder.of(JadeChinaCupBlockEntity::new, JADE_CHINA_CUP.get()).build(null));


    //水壶
    public static final String KETTLE_ID = "kettle";
    public static final RegistryObject<KettleEntityBlock> KETTLE = BLOCKS.register(KETTLE_ID, KettleEntityBlock::new);
    public static final RegistryObject<BlockEntityType<KettleBlockEntity>> KETTLE_ENTITY = BLOCK_ENTITIES.register(KETTLE_ID, () -> BlockEntityType.Builder.of(KettleBlockEntity::new, KETTLE.get()).build(null));

    //汤锅
    public static final String SOUP_POT_ID = "soup_pot";
    public static final RegistryObject<SoupPotEntityBlock> SOUP_POT = BLOCKS.register(SOUP_POT_ID, SoupPotEntityBlock::new);
    public static final RegistryObject<BlockEntityType<SoupPotBlockEntity>> SOUP_POT_ENTITY = BLOCK_ENTITIES.register(SOUP_POT_ID, () -> BlockEntityType.Builder.of(SoupPotBlockEntity::new, SOUP_POT.get()).build(null));

}
