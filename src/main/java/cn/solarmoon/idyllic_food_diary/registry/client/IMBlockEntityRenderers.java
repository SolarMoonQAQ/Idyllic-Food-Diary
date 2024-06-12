package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.CupBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate.ServicePlateBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.stove.StoveBlockRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockRenderer;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;

public class IMBlockEntityRenderers {

    public static void register() {
        IMBlockEntities.LITTLE_CUP.renderer(() -> CupBlockRenderer::new);
        IMBlockEntities.STEAMER.renderer(() -> SteamerBlockRenderer::new);
        IMBlockEntities.PLATE.renderer(() -> ServicePlateBlockRenderer::new);
        IMBlockEntities.GRILL.renderer(() -> GrillBlockRenderer::new);
        IMBlockEntities.CUTTING_BOARD.renderer(() -> CuttingBoardBlockRenderer::new);
        IMBlockEntities.COOKING_POT.renderer(() -> CookingPotBlockRenderer::new);
        IMBlockEntities.FOOD.renderer(() -> FoodBlockRenderer::new);
        IMBlockEntities.STOVE.renderer(() -> StoveBlockRenderer::new);
    }

}
