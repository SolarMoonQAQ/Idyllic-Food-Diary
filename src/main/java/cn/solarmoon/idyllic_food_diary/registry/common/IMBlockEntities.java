package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AttachmentBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.LongContainerBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate.LongPlateBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup.LittleCupBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven.OvenBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.ContainerBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar.SpiceJarBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.winnowing_basket.WinnowingBasketBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock;
import cn.solarmoon.solarmoon_core.api.entry.common.BlockEntityEntry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class IMBlockEntities {
    public static void register() {}

    // 烤箱
    public static final BlockEntityEntry<OvenBlockEntity> OVEN = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("oven")
            .bound(OvenBlockEntity::new)
            .validBlock(IMBlocks.OVEN::get)
            .build();

    // 簸箕
    public static final BlockEntityEntry<WinnowingBasketBlockEntity> WINNOWING_BASKET = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("winnowing_basket")
            .bound(WinnowingBasketBlockEntity::new)
            .validBlock(IMBlocks.WINNOWING_BASKET::get)
            .build();

    // 炒锅
    public static final BlockEntityEntry<WokBlockEntity> WOK = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("wok")
            .bound(WokBlockEntity::new)
            .validBlock(IMBlocks.WOK::get)
            .build();

    // 食物
    public static final BlockEntityEntry<FoodBlockEntity> FOOD = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("food")
            .bound(FoodBlockEntity::new)
            .validBlocks(() -> IdyllicFoodDiary.REGISTRY.blockRegister.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(block -> block instanceof FoodEntityBlock)
                    .toArray(Block[]::new))
            .build();

    //小容量杯子
    public static final BlockEntityEntry<LittleCupBlockEntity> LITTLE_CUP = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("little_cup")
            .bound(LittleCupBlockEntity::new)
            .validBlocks(() -> new Block[]{IMBlocks.CELADON_CUP.get(), IMBlocks.JADE_CHINA_CUP.get()})
            .build();

    //水壶
    public static final BlockEntityEntry<KettleBlockEntity> KETTLE = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("kettle")
            .bound(KettleBlockEntity::new)
            .validBlock(IMBlocks.KETTLE::get)
            .build();

    //汤锅
    public static final BlockEntityEntry<CookingPotBlockEntity> COOKING_POT = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("cooking_pot")
            .bound(CookingPotBlockEntity::new)
            .validBlock(IMBlocks.COOKING_POT::get)
            .build();

    //砧板
    public static final BlockEntityEntry<CuttingBoardBlockEntity> CUTTING_BOARD = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("cutting_board")
            .bound(CuttingBoardBlockEntity::new)
            .validBlock(IMBlocks.CUTTING_BOARD::get)
            .build();

    //烧烤架
    public static final BlockEntityEntry<GrillBlockEntity> GRILL = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("grill")
            .bound(GrillBlockEntity::new)
            .validBlock(IMBlocks.GRILL::get)
            .build();

    //一般容器
    public static final BlockEntityEntry<ContainerBlockEntity> CONTAINER = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("container")
            .bound((pos, state) -> new ContainerBlockEntity(9, pos, state))
            .validBlocks(() -> new Block[] {
                    IMBlocks.WOODEN_PLATE.get(), IMBlocks.PORCELAIN_PLATE.get()
            })
            .build();

    //长容器
    public static final BlockEntityEntry<LongContainerBlockEntity> LONG_CONTAINER = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("long_container")
            .bound((pos, state) -> new LongContainerBlockEntity(LongPlateBlockRenderer.maxElement, pos, state))
            .validBlocks(() -> new Block[] {
                    IMBlocks.LONG_WOODEN_PLATE.get(), IMBlocks.LONG_PORCELAIN_PLATE.get()
            })
            .build();

    public static final BlockEntityEntry<AttachmentBlockEntity> ATTACHMENT = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("attachment")
            .bound(AttachmentBlockEntity::new)
            .validBlocks(() -> new Block[] {
                    IMBlocks.LONG_WOODEN_PLATE.get(), IMBlocks.LONG_PORCELAIN_PLATE.get()
            })
            .build();

    //蒸笼
    public static final BlockEntityEntry<SteamerBlockEntity> STEAMER = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("steamer")
            .bound(SteamerBlockEntity::new)
            .validBlock(IMBlocks.STEAMER::get)
            .build();

    //调料罐
    public static final BlockEntityEntry<SpiceJarBlockEntity> SPICE_JAR = IdyllicFoodDiary.REGISTRY.blockEntity()
            .id("spice_jar")
            .bound((pos, state) -> new SpiceJarBlockEntity(16, pos, state))
            .validBlock(IMBlocks.SPICE_JAR::get)
            .build();

}
