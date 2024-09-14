package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneInventory
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper
import java.util.*

@JvmRecord
data class GrindingRecipe(
    val ingredient: Ingredient,
    val inputFluid: FluidStack,
    val time: Int,
    val result: ItemStack,
    val outputFluid: FluidStack
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.GRINDING

    class Serializer : RecipeSerializer<GrindingRecipe> {
        override fun codec(): MapCodec<GrindingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter { it.ingredient },
                    FluidStack.OPTIONAL_CODEC.fieldOf("input_fluid").forGetter { it.inputFluid },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result },
                    FluidStack.OPTIONAL_CODEC.fieldOf("output_fluid").forGetter { it.outputFluid }
                ).apply(it, ::GrindingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, GrindingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, GrindingRecipe::ingredient,
                FluidStack.OPTIONAL_STREAM_CODEC, GrindingRecipe::inputFluid,
                ByteBufCodecs.INT, GrindingRecipe::time,
                ItemStack.OPTIONAL_STREAM_CODEC, GrindingRecipe::result,
                FluidStack.OPTIONAL_STREAM_CODEC, GrindingRecipe::outputFluid,
                ::GrindingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inventory: IItemHandler, val tanks: List<FluidTank>) : SingleTimeRecipeProcessor<RecipeWrapper, GrindingRecipe>(be) {

        var flowing = false

        override fun isRecipeMatch(recipe: GrindingRecipe): Boolean {
            return tanks[0].drain(recipe.inputFluid, FluidAction.SIMULATE).amount == recipe.inputFluid.amount
        }

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                val anim = be.getData(SparkAttachments.ANIMTICKER)
                if (anim.timers[MillstoneBlockEntity.ANIM_ROTATION]!!.isTiming) {
                    time++
                    recipeTime = it.value.time
                    if (time > recipeTime) {
                        if (isRecipeSmooth()) {
                            inventory.getStackInSlot(0).shrink(1)
                            (inventory as MillstoneInventory).realInsert(1, it.value.result.copy(), false)
                            tanks[0].drain(it.value.inputFluid, FluidAction.EXECUTE)
                            tanks[1].fill(it.value.outputFluid.copy(), FluidAction.EXECUTE)
                            time = 0
                            recipeTime = 0
                        }
                    }
                    return true
                }
                return false
            } ?: let {
                time = 0
                recipeTime = 0
                return false
            }
        }

        /**
         * @return 配方是否能顺利进行（1.配方匹配 2.拥有足够空间给所有配方产物）
         */
        fun isRecipeSmooth(): Boolean {
            return findRecipe()?.let {
                it.value.ingredient.test(inventory.getStackInSlot(0))
                        && tanks[1].fill(it.value.outputFluid, FluidAction.SIMULATE) == it.value.outputFluid.amount
                        && !ItemStack.matches((inventory as MillstoneInventory).realInsert(1, it.value.result.copy(), true), it.value.result.copy())
            } == true
        }

        /**
         * @return 判断物品能否放入用
         */
        fun findRecipe(match: ItemStack): Boolean {
            val level: Level = be.level ?: return false
            val recipes = level.recipeManager.getAllRecipesFor(IFDRecipes.GRINDING.type.get())
            return recipes.stream().filter { recipe -> recipe.value.ingredient.test(match) }.findFirst().isPresent
        }

        /**
         * 获取可流放入当前输出液体的储罐
         */
        fun getFluidReceiver(): Optional<IFluidHandler> {
            val state = be.blockState
            var pos = be.blockPos
            val side = state.getValue(IHorizontalFacingState.FACING)
            val level = be.level ?: return Optional.empty<IFluidHandler>()
            pos = pos.relative(side)
            // 前方不能有阻挡
            if (!level.getBlockState(pos).isAir) return Optional.empty()
            pos = pos.below()
            // 这里写上限定方块条件
            return FluidUtil.getFluidHandler(level, pos, Direction.UP)
        }

        /**
         * 尝试输出液体到外部储罐
         */
        fun transferOfFluid() {
            if (getFluidReceiver().isEmpty) flowing = false
            getFluidReceiver().ifPresent { t: IFluidHandler ->
                // 必须能传递液体才行
                val s = FluidAction.SIMULATE
                if (t.fill(tanks[1].drain(1, s), s) == 1) {
                    flowing = true
                    t.fill(tanks[1].drain(1, FluidAction.EXECUTE), FluidAction.EXECUTE)
                } else flowing = false
            }
        }

        override fun save(tag: CompoundTag, registries: HolderLookup.Provider) {
            super.save(tag, registries)
            tag.putBoolean(getName() + "Flowing", flowing)
        }

        override fun load(tag: CompoundTag, registries: HolderLookup.Provider) {
            super.load(tag, registries)
            flowing = tag.getBoolean(getName() + "Flowing")
        }

        override fun getRecipeType(): RecipeType<GrindingRecipe> = IFDRecipes.GRINDING.type.get()

    }

    class JsonBuilder(val r: () -> GrindingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.FLUID.getKey(r.invoke().outputFluid.fluid)

        override val prefix: String
            get() = IFDRecipes.GRINDING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}