package cn.solarmoon.idyllic_food_diary.core.client.registry;

import cn.solarmoon.idyllic_food_diary.core.client.renderer.block_entity.*;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;

public class IMBlockEntityRenderers {

    public static void register() {
        IMBlockEntities.LITTLE_CUP.renderer(() -> LittleCupRenderer::new);
        IMBlockEntities.STEAMER_BASE.renderer(() -> SteamerBaseRenderer::new);
        IMBlockEntities.STEAMER.renderer(() -> SteamerRenderer::new);
        IMBlockEntities.PLATE.renderer(() -> ServicePlateRenderer::new);
        IMBlockEntities.GRILL.renderer(() -> GrillRenderer::new);
        IMBlockEntities.CUTTING_BOARD.renderer(() -> CuttingBoardRenderer::new);
        IMBlockEntities.COOKING_POT.renderer(() -> CookingPotRenderer::new);
        IMBlockEntities.FOOD.renderer(() -> FoodBlockRenderer::new);
        IMBlockEntities.STOVE.renderer(() -> StoveRenderer::new);
    }

}
