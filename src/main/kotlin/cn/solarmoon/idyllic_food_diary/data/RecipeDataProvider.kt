package cn.solarmoon.idyllic_food_diary.data

import cn.solarmoon.idyllic_food_diary.element.recipe.ChoppingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.GrindingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.RollingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.SteamingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.StirFryRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.WaterBoilingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.StirFryStage
import cn.solarmoon.idyllic_food_diary.feature.util.MessageUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.spark_core.api.data.element.AttributeData
import cn.solarmoon.spark_core.api.data.element.ChanceResult
import cn.solarmoon.spark_core.feature.inlay.AttributeForgingRecipe
import cn.solarmoon.spark_core.registry.common.SparkAttributes
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import java.util.concurrent.CompletableFuture

class RecipeDataProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries) {

    override fun buildRecipes(recipeOutput: RecipeOutput) {
        GrindingRecipe.JsonBuilder {
            GrindingRecipe(
                Ingredient.of(Items.IRON_INGOT),
                FluidStack(Fluids.WATER, 50),
                20,
                ItemStack(IFDItems.STOVE_LID),
                FluidStack(Fluids.LAVA, 50)
            )
        }.save(recipeOutput)
        StirFryRecipe.JsonBuilder {
            StirFryRecipe(
                listOf(
                    StirFryStage(listOf(Ingredient.of(Items.POTATO), Ingredient.of(Items.CHICKEN)), FluidStack.EMPTY, 100, 3, false)
                ),
                ItemStack(IFDItems.BEGGARS_CHICKEN),
                Ingredient.of(IFDItemTags.PLATE),
                10,
                MessageUtil.PLATE
            )
        }.save(recipeOutput)
        RollingRecipe.JsonBuilder {
            RollingRecipe(
                Ingredient.of(Blocks.GRASS_BLOCK),
                40,
                Blocks.IRON_BLOCK
            )
        }.save(recipeOutput)
        RollingRecipe.JsonBuilder {
            RollingRecipe(
                Ingredient.of(Blocks.GRASS_BLOCK),
                40,
                Blocks.DIAMOND_BLOCK
            )
        }.save(recipeOutput)
        RollingRecipe.JsonBuilder {
            RollingRecipe(
                Ingredient.of(Blocks.GRASS_BLOCK),
                40,
                Blocks.GOLD_BLOCK
            )
        }.save(recipeOutput)
        RollingRecipe.JsonBuilder {
            RollingRecipe(
                Ingredient.of(Blocks.GRASS_BLOCK),
                40,
                Blocks.EMERALD_BLOCK
            )
        }.save(recipeOutput)
        ChoppingRecipe.JsonBuilder {
            ChoppingRecipe(
                Ingredient.of(Blocks.GRASS_BLOCK),
                listOf(
                    ChanceResult(ItemStack(Items.IRON_INGOT), 0.5f)
                )
            )
        }.save(recipeOutput)
        SteamingRecipe.JsonBuilder {
            SteamingRecipe(
                Ingredient.of(IFDItems.WHEAT_DOUGH.get()),
                100,
                ItemStack(IFDItems.STEAMED_BUN)
            )
        }.save(recipeOutput)
        WaterBoilingRecipe.JsonBuilder {
            WaterBoilingRecipe(Fluids.WATER, 400)
        }.save(recipeOutput)
    }

}