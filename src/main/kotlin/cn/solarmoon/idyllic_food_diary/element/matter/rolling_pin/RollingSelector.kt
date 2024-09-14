package cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin

import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.RecipeSelectData
import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.RecipeSelector
import cn.solarmoon.idyllic_food_diary.registry.client.IFDGuis
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult

class RollingSelector: RecipeSelector(IFDGuis.ROLLING) {

    override fun condition(
        player: LocalPlayer,
        level: Level,
        selectData: RecipeSelectData,
        mainItem: ItemStack,
        hitResult: BlockHitResult
    ): Boolean {
        return RollingPinItem.findRecipe(level.getBlockState(hitResult.blockPos).block, player.level(), 0) != null
    }

}