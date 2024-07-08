package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate.LongPlateBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup.LittleCupBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate.PlateBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockRenderer;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;

public class IMBlockEntityRenderers {

    public static void register() {
        IMBlockEntities.LITTLE_CUP.renderer(() -> LittleCupBlockRenderer::new);
        IMBlockEntities.STEAMER.renderer(() -> SteamerBlockRenderer::new);
        IMBlockEntities.CONTAINER.renderer(() -> PlateBlockRenderer::new);
        IMBlockEntities.LONG_CONTAINER.renderer(() -> LongPlateBlockRenderer::new);
        IMBlockEntities.GRILL.renderer(() -> GrillBlockRenderer::new);
        IMBlockEntities.CUTTING_BOARD.renderer(() -> CuttingBoardBlockRenderer::new);
        IMBlockEntities.COOKING_POT.renderer(() -> CookingPotBlockRenderer::new);
        IMBlockEntities.FOOD.renderer(() -> FoodBlockRenderer::new);
        IMBlockEntities.WOK.renderer(() -> WokBlockRenderer::new);
    }

}
