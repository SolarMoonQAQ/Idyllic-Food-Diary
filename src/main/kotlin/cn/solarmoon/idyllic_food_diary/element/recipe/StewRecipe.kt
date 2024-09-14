package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.IPlateable
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.data.SerializeHelper
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.common.util.RecipeMatcher
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class StewRecipe(
    val ingredients: List<Ingredient>,
    val inputFluid: FluidStack,
    val time: Int,
    val result: ItemStack,
    val container: Ingredient,
    val exp: Int
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.STEW

    class Serializer : RecipeSerializer<StewRecipe> {
        override fun codec(): MapCodec<StewRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter { it.ingredients },
                    FluidStack.OPTIONAL_CODEC.fieldOf("input_fluid").forGetter { it.inputFluid },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result },
                    Ingredient.CODEC.fieldOf("container").forGetter { it.container },
                    Codec.INT.fieldOf("exp").forGetter { it.exp }
                ).apply(it, ::StewRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, StewRecipe> {
            return StreamCodec.composite(
                SerializeHelper.INGREDIENT.LIST_STREAM_CODEC, StewRecipe::ingredients,
                FluidStack.OPTIONAL_STREAM_CODEC, StewRecipe::inputFluid,
                ByteBufCodecs.INT, StewRecipe::time,
                ItemStack.OPTIONAL_STREAM_CODEC, StewRecipe::result,
                Ingredient.CONTENTS_STREAM_CODEC, StewRecipe::container,
                ByteBufCodecs.INT, StewRecipe::exp,
                ::StewRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inv: ItemStackHandler, val tank: FluidTank): SingleTimeRecipeProcessor<RecipeWrapper, StewRecipe>(be), IPlateable {

        override var exp = 0
        override var result: ItemStack = ItemStack.EMPTY
        override var container: Ingredient = Ingredient.EMPTY
        override fun getBlockEntity() = be

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                if (!hasResult()) {
                    time++
                    recipeTime = it.value.time
                    if (time > recipeTime) {
                        //输出物
                        if (!it.value.result.isEmpty) {
                            setPending(it.value.result.copy(), it.value.container)
                        }
                        FluidHandlerHelper.clearTank(tank)
                        exp = it.value.exp
                        time = 0
                        recipeTime = 0
                        ItemStackHandlerHelper.clearInv(inv)
                        be.setChanged()
                    }
                    return true
                }
            } ?: let {
                time = 0
                recipeTime = 0
            }
            return false
        }

        override fun isRecipeMatch(recipe: StewRecipe): Boolean {
            /*
             * 要求：
             * 输入物完全匹配
             * 输入液体及量完全匹配
             * 下方为热源
             */
            val stacks = ItemStackHandlerHelper.getStacks(inv)
            if (RecipeMatcher.findMatches(stacks, recipe.ingredients) != null) {
                val ctStack = tank.fluid
                if (FluidHandlerHelper.isMatch(ctStack, recipe.inputFluid, true, false)) {
                    return HeatSourceUtil.isOnHeatSource(be)
                }
            }
            return false
        }

        override fun getRecipeType(): RecipeType<StewRecipe> = IFDRecipes.STEW.type.get()

    }

    class JsonBuilder(private val r: () -> StewRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().result.item)

        override val prefix: String
            get() = IFDRecipes.STEW.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}