package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.data.SerializeHelper
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessor
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class IngredientHandlingRecipe(
    val ingredients: List<Ingredient>,
    val container: Ingredient,
    val isInOrder: Boolean,
    val result: ItemStack
): IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.INGREDIENT_HANDLING

    class Serializer : RecipeSerializer<IngredientHandlingRecipe> {
        override fun codec(): MapCodec<IngredientHandlingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter { it.ingredients },
                    Ingredient.CODEC.fieldOf("container").forGetter { it.container },
                    Codec.BOOL.fieldOf("order").forGetter { it.isInOrder },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result }
                ).apply(it, ::IngredientHandlingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, IngredientHandlingRecipe> {
            return StreamCodec.composite(
                SerializeHelper.INGREDIENT.LIST_STREAM_CODEC, IngredientHandlingRecipe::ingredients,
                Ingredient.CONTENTS_STREAM_CODEC, IngredientHandlingRecipe::container,
                ByteBufCodecs.BOOL, IngredientHandlingRecipe::isInOrder,
                ItemStack.OPTIONAL_STREAM_CODEC, IngredientHandlingRecipe::result,
                ::IngredientHandlingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inv: ItemStackHandler): RecipeProcessor<RecipeWrapper, IngredientHandlingRecipe>(be) {

        /**
         * 尝试当玩家拿着对应道具右键时转化输出
         */
        fun trOutputResult(heldItem: ItemStack, player: Player, pos: BlockPos, level: Level): Boolean {
            findRecipe()?.let {
                if (it.container.test(heldItem)) {
                    ItemStackHandlerHelper.clearInv(inv)
                    val result = it.result.copy()
                    ItemStackHandlerHelper.insertItem(inv, result)
                    if (!heldItem.isEmpty) result.set(IFDDataComponents.FOOD_CONTAINER, FoodContainer(heldItem))
                    if (!player.isCreative) heldItem.shrink(1)
                    val center = pos.center
                    val randomSource = player.random
                    repeat(randomSource.nextInt(2, 5)) {
                        level.addParticle(ParticleTypes.END_ROD, pos.x + randomSource.nextDouble(), center.y - 0.5, pos.z + randomSource.nextDouble(), 0.0, 0.1, 0.0)
                    }
                    level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 1f, 2f)
                    be.setChanged()
                    return true
                }
            }
            return false
        }

        fun findRecipe(): IngredientHandlingRecipe? {
            val level = be.level ?: return null
            val recipes = level.recipeManager.getAllRecipesFor(IFDRecipes.INGREDIENT_HANDLING.type.get())
            for (recipe in recipes.map { it.value }) {
                var pass = true
                if (ItemStackHandlerHelper.getStacks(inv).size == recipe.ingredients.size) {
                    if (recipe.isInOrder) {
                        for (i in recipe.ingredients.indices) {
                            if (!recipe.ingredients[i].test(inv.getStackInSlot(i))) {
                                pass = false
                                break
                            }
                        }
                    } else {
                        val matched = BooleanArray(recipe.ingredients.size)
                        for (i in ItemStackHandlerHelper.getStacks(inv).indices) {
                            var found = false
                            for (j in recipe.ingredients.indices) {
                                if (!matched[j] && recipe.ingredients[j].test(inv.getStackInSlot(i))) {
                                    matched[j] = true
                                    found = true
                                    break
                                }
                            }
                            if (!found) {
                                pass = false
                                break
                            }
                        }
                    }
                    if (pass) {
                        return recipe
                    }
                }
            }
            return null
        }

        fun hasOutput(): Boolean = findRecipe() != null

        override fun getRecipeType(): RecipeType<IngredientHandlingRecipe> = IFDRecipes.INGREDIENT_HANDLING.type.get()

        override fun save(tag: CompoundTag, registries: HolderLookup.Provider) {
        }

        override fun load(tag: CompoundTag, registries: HolderLookup.Provider) {
        }

    }

    class JsonBuilder(val r: () -> IngredientHandlingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().result.item)

        override val prefix: String
            get() = IFDRecipes.INGREDIENT_HANDLING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}