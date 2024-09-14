package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove
import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.IPlateable
import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.StirFryStage
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.idyllic_food_diary.registry.common.IFDSounds
import cn.solarmoon.spark_core.api.attachment.animation.Timer
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper
import java.util.*
import kotlin.math.sqrt

@JvmRecord
data class StirFryRecipe(
    val stirFryStages: List<StirFryStage>,
    val result: ItemStack,
    val container: Ingredient,
    val exp: Int
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.STIR_FRY

    val firstStage: StirFryStage
        get() = stirFryStages[0]


    class Serializer : RecipeSerializer<StirFryRecipe> {
        override fun codec(): MapCodec<StirFryRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    StirFryStage.LIST_CODEC.fieldOf("stir_fry_stages").forGetter { it.stirFryStages },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result },
                    Ingredient.CODEC.fieldOf("container").forGetter { it.container },
                    Codec.INT.fieldOf("exp").forGetter { it.exp }
                ).apply(it, ::StirFryRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, StirFryRecipe> {
            return StreamCodec.composite(
                StirFryStage.LIST_STREAM_CODEC, StirFryRecipe::stirFryStages,
                ItemStack.OPTIONAL_STREAM_CODEC, StirFryRecipe::result,
                Ingredient.CONTENTS_STREAM_CODEC, StirFryRecipe::container,
                ByteBufCodecs.INT, StirFryRecipe::exp,
                ::StirFryRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inv: ItemStackHandler, val tank: FluidTank): SingleTimeRecipeProcessor<RecipeWrapper, StirFryRecipe>(be), IPlateable {

        override var exp = 0
        override var result: ItemStack = ItemStack.EMPTY
        override var container: Ingredient = Ingredient.EMPTY
        override fun getBlockEntity() = be

        /**
         * 当前翻炒次数
         */
        var fryCount = 0

        /**
         * 是否可以翻炒
         */
        var canStirFry = false

        /**
         * 当前所匹配的配方
         */
        var presentRecipe: RecipeHolder<StirFryRecipe>? = null

        /**
         * 当前翻炒阶段（显示上需要以此值+1）
         */
        var presentStageIndex = 0

        /**
         * 预输出物品的预输入物品，用于模拟物品预输出，作为过渡到真正的预输出方法中
         */
        var pendingItem = ItemStack.EMPTY

        override fun tryWork(): Boolean {
            // 如果当前物品满足任意配方的阶段0，则保存该配方
            findRecipe()?.let { presentRecipe = it }

            // 如果当前物品没有满足当前配方阶段-1的所有物品，则删除保存的配方
            presentRecipe?.let {
                val ingredients: MutableList<Ingredient> = ArrayList()
                val stage = maxOf(0, presentStageIndex - 1) // 同时，第一阶段是必须随时满足的
                for (i in 0..stage) {
                    ingredients.addAll(it.value.stirFryStages[i].ingredients)
                }
                // 下面同时检测了物品和需要材料长度是否一致，否则可能出现同一个材料匹配多个物品的情况
                if (!ingredients.all { ing -> ItemStackHandlerHelper.getStacks(inv).stream().anyMatch(ing) && ItemStackHandlerHelper.getStacks(inv).size >= ingredients.size }) {
                    presentRecipe = null
                }
            }

            // 有配方（也就是在首次条件中完全满足了阶段0），进入烹饪
            presentRecipe?.let {
                getPresentFryStage()?.let { stage -> // 这里当前阶段不为null，意味着还在配方所需阶段范围内
                    recipeTime = stage.time
                    // 对每个阶段进行内容物检查，要求：满足所有阶段物品所需之和
                    val ingredients: MutableList<Ingredient> = ArrayList()
                    for (i in 0..presentStageIndex) {
                        ingredients.addAll(it.value.stirFryStages[i].ingredients)
                    }
                    // 当物品和液体全匹配后，进行该阶段的正式烹饪
                    if (ingredients.stream().allMatch { `in`: Ingredient? -> ItemStackHandlerHelper.getStacks(inv).stream().anyMatch(`in`) && ItemStackHandlerHelper.getStacks(inv).size == ingredients.size }
                        && isFluidMatch()) {
                        if (time > getPresentFryStage()!!.time) {
                            canStirFry = true // 设置可以翻炒
                            if (isAnimFin() && fryCount >= stage.fryCount) { // 最终步骤完成
                                pendingItem = pendingItem.takeIf { !it.isEmpty } ?: it.value.result.copy() // 传递宝友
                                if (!stage.keepFluid) FluidHandlerHelper.clearTank(tank)
                                time = 0
                                recipeTime = 0
                                fryCount = 0 // 重置各个计数
                                presentStageIndex++ // 到下个阶段
                                canStirFry = false
                                be.setChanged()
                            }
                        } else time++
                    }
                } ?: run { // 此处就代表当前阶段已经超出配方最大阶段，表示所有阶段已满足
                    setPending(pendingItem, it.value.container)
                    exp = it.value.exp
                    ItemStackHandlerHelper.clearInv(inv)
                    be.setChanged()
                }
                return true
            } ?: let {
                presentStageIndex = 0
                time = 0
                recipeTime = 0
                fryCount = 0
                //重置各个计数
                return false
            }
        }

        override fun isRecipeMatch(recipe: StirFryRecipe): Boolean {
            val inMatch = ItemStackHandlerHelper.getStacks(inv).all {
                recipe.firstStage.ingredients.any { ingredient ->
                    ingredient.test(it)
                }
            }
            val fluidMatch = FluidHandlerHelper.isMatch(tank.fluid, recipe.firstStage.fluidStack, true, false)
            val heat = HeatSourceUtil.isOnHeatSource(be)
            return inMatch && fluidMatch && heat
        }

        override fun getRecipeType(): RecipeType<StirFryRecipe> = IFDRecipes.STIR_FRY.type.get()

        override fun isWorking(): Boolean {
            return presentRecipe != null && getPresentFryStage() != null;
        }

        fun doStirFry(): Boolean {
            if (!isAnimFin() || getPresentFryStage() == null || fryCount >= getPresentFryStage()!!.fryCount) return false

            val level = be.level ?: return false

            var flag = false

            for (i in ItemStackHandlerHelper.getStacks(inv).indices) {
                val name = "fry$i"
                val anim = be.getData(SparkAttachments.ANIMTICKER)
                anim.timers.getOrPut(name) { Timer() }.maxTime = getFryTime(ItemStackHandlerHelper.getStacks(inv).size).toFloat()
                if (canStirFry && !anim.timers[name]!!.isTiming) {
                    anim.fixedValues["rotRandom$i"] = Random().nextFloat() // 设置炒菜随机旋转角
                    anim.fixedValues["maxHeight$i"] = getFryHeight(i)
                    val finalI: Int = i
                    anim.timers[name]!!.onStop = {
                        var posYOffset = 0.0
                        val block = be.blockState.block
                        if (block is IBuiltInStove) {
                            posYOffset = block.getYOffset(be.blockState)
                        }
                        // 每一片食材落下后产生火花
                        val xInRange = 2 / 16f + Random().nextDouble() * 12 / 16
                        val zInRange = 2 / 16f + Random().nextDouble() * 12 / 16 // 保证粒子起始点在锅内
                        val vi = (Random().nextDouble() - 0.5) / 5
                        level.addParticle(ParticleTypes.SMALL_FLAME, be.blockPos.x + xInRange, be.blockPos.y + (1.5 + finalI * 0.25) / 16f + posYOffset, be.blockPos.z + zInRange, vi, 0.1, vi)
                    }
                    anim.timers[name]!!.start()
                    flag = true
                }
            }

            if (flag) {
                fryCount++
                level.playSound(null, be.blockPos, IFDSounds.STIR_SIZZLE.get(), SoundSource.BLOCKS)
                return true
            }

            return false
        }

        /**
         * @return 每道菜翻炒能到达的最大高度
         */
        fun getFryHeight(index: Int): Float {
            return (12 / 16f + 0.25 * index).toFloat()
        }

        /**
         * @return 自由落体时间
         */
        fun getFryTime(index: Int): Int {
            val g = 9.8
            val t = sqrt(getFryHeight(index) / g)
            return (t * 30).toInt()
        }

        fun isAnimFin(): Boolean {
            val anim = be.getData(SparkAttachments.ANIMTICKER)
            return anim.timers.values.all { !it.isTiming }
        }

        /**
         * @return 当前液体是否匹配当前阶段所需的液体
         */
        fun isFluidMatch(): Boolean {
            if (getPresentFryStage() != null) {
                return FluidHandlerHelper.isMatch(tank.fluid, getPresentFryStage()!!.fluidStack, true, false)
            }
            return false
        }

        /**
         * @return 获取当前的翻炒阶段
         */
        fun getPresentFryStage(): StirFryStage? {
            presentRecipe?.let {
                if (presentStageIndex < it.value.stirFryStages.size) {
                    return it.value.stirFryStages[presentStageIndex]
                }
            }
            return null
        }

        fun getFryStage(stage: Int): StirFryStage? {
            return presentRecipe?.value?.stirFryStages?.get(stage)
        }

        override fun save(tag: CompoundTag, registries: HolderLookup.Provider) {
            super.save(tag, registries)
            tag.putInt("FryCount", fryCount)
            tag.putInt("FryIndex", presentStageIndex)
            tag.putBoolean("FryCanStir", canStirFry)
            tag.put("FryPendingItem", pendingItem.saveOptional(registries))
            presentRecipe?.let { tag.putString("Recipe", it.id().toString()) }
        }

        override fun load(tag: CompoundTag, registries: HolderLookup.Provider) {
            super.load(tag, registries)
            fryCount = tag.getInt("FryCount")
            presentStageIndex = tag.getInt("FryIndex")
            canStirFry = tag.getBoolean("FryCanStir")
            pendingItem = ItemStack.parseOptional(registries, tag.getCompound("FryPendingItem"))
            be.level?.let {
                tag.getString("Recipe").takeIf { it.isNotEmpty() }?.let { id ->
                    val rop = it.recipeManager.byKey(ResourceLocation.parse(id))
                    @Suppress("UNCHECKED_CAST")
                    rop.ifPresent { r -> presentRecipe = r as RecipeHolder<StirFryRecipe> }
                }
            }
        }

    }

    class JsonBuilder(val r: () -> StirFryRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().result.item)

        override val prefix: String
            get() = IFDRecipes.STIR_FRY.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}