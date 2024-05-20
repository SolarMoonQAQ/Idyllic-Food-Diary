package cn.solarmoon.idyllic_food_diary.core.common.registry;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.FoodEntityBlock;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.*;
import cn.solarmoon.solarmoon_core.api.common.registry.BlockEntityEntry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class IMBlockEntities {
    public static void register() {}

    // 食物
    public static final BlockEntityEntry<FoodBlockEntity> FOOD = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("food")
            .bound(FoodBlockEntity::new)
            .validBlock(() -> IdyllicFoodDiary.REGISTRY.blockRegister.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(block -> block instanceof FoodEntityBlock)
                    .toArray(Block[]::new))
            .build();


    //小容量杯子
    public static final BlockEntityEntry<CupBlockEntity> LITTLE_CUP = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("little_cup")
            .bound((pos, state) -> new CupBlockEntity(250, 1, 1, pos, state))
            .validBlock(() -> new Block[]{IMBlocks.CELADON_CUP.get(), IMBlocks.JADE_CHINA_CUP.get()})
            .build();

    //水壶
    public static final BlockEntityEntry<KettleBlockEntity> KETTLE = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("kettle")
            .bound((pos, state) -> new KettleBlockEntity(1000, 3, 1, pos, state))
            .validBlock(() -> new Block[]{IMBlocks.KETTLE.get()})
            .build();

    //汤锅
    public static final BlockEntityEntry<CookingPotBlockEntity> SOUP_POT = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("soup_pot")
            .bound((pos, state) -> new CookingPotBlockEntity(1000, 9, 1, pos, state))
            .validBlock(() -> new Block[]{IMBlocks.SOUP_POT.get()})
            .build();

    //砧板
    public static final BlockEntityEntry<CuttingBoardBlockEntity> CUTTING_BOARD = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("cutting_board")
            .bound(CuttingBoardBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.CUTTING_BOARD.get()})
            .build();

    //烧烤架
    public static final BlockEntityEntry<GrillBlockEntity> GRILL = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("grill")
            .bound(GrillBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.GRILL.get()})
            .build();

    //餐盘
    public static final BlockEntityEntry<ServicePlateBlockEntity> PLATE = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("service_plate")
            .bound(ServicePlateBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.SERVICE_PLATE.get()})
            .build();

    //蒸笼
    public static final BlockEntityEntry<SteamerBlockEntity> STEAMER = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("steamer")
            .bound(SteamerBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.STEAMER.get()})
            .build();

    //蒸笼底座
    public static final BlockEntityEntry<SteamerBaseBlockEntity> STEAMER_BASE = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("steamer_base")
            .bound(SteamerBaseBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.STEAMER_BASE.get()})
            .build();

    //调料罐
    public static final BlockEntityEntry<SpiceJarBlockEntity> SPICE_JAR = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("spice_jar")
            .bound((pos, state) -> new SpiceJarBlockEntity(16, pos, state))
            .validBlock(() -> new Block[]{IMBlocks.STEAMER_BASE.get()})
            .build();

}
