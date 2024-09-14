package cn.solarmoon.idyllic_food_diary.element.matter.cleaver

import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinItem
import cn.solarmoon.idyllic_food_diary.element.recipe.ChoppingRecipe
import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.RecipeSelectData
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.idyllic_food_diary.registry.common.IFDSounds
import cn.solarmoon.spark_core.api.util.DropUtil
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.SwordItem.createAttributes
import net.minecraft.world.item.SwordItem.createToolProperties
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level

class CleaverItem(
    tier: Tier = Tiers.IRON,
    properties: Properties = Properties().attributes(createAttributes(Tiers.IRON, 3, -2.4f)),
    toolComponentData: Tool = createToolProperties()
): SwordItem(tier, properties, toolComponentData) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player ?: return InteractionResult.PASS
        val level = context.level
        val pos = context.clickedPos
        val item = context.itemInHand
        val block = level.getBlockState(pos).block
        val index = item.getOrDefault(IFDDataComponents.RECIPE_SELECTION, RecipeSelectData.EMPTY).getIndex(block)
        val input = ItemStack(block)
        findRecipe(input, level, index)?.let {
            level.destroyBlock(pos, false)
            it.value.getRolledResults(player).forEach { DropUtil.summonDrop(it, level, pos) }
            level.playSound(null, pos, IFDSounds.CHOPPING.value(), SoundSource.PLAYERS)
            return InteractionResult.SUCCESS
        }
        return super.useOn(context)
    }

    companion object {
        @JvmStatic
        fun findRecipes(input: ItemStack, level: Level): List<RecipeHolder<ChoppingRecipe>> {
            return level.recipeManager.getAllRecipesFor(IFDRecipes.CHOPPING.type.get()).filter { it.value().input.test(input) }
        }

        /**
         * 先找出所有同输入的配方，然后根据[selectIndex]选择具体哪一个配方
         *
         * 如果只是想通过此方法判断是否输入匹配，将[selectIndex]设为0即可
         */
        @JvmStatic
        fun findRecipe(input: ItemStack, level: Level, selectIndex: Int): RecipeHolder<ChoppingRecipe>? {
            var sli = selectIndex
            val matchRecipes = findRecipes(input, level)
            if (sli !in 0 until matchRecipes.size ) sli = 0 // 序列矫正，如果超出可用列表范围就改为0防止错误
            return if (matchRecipes.isEmpty()) null else matchRecipes[sli]
        }
    }

}