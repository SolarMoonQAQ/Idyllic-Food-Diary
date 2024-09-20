package cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin

import cn.solarmoon.idyllic_food_diary.data.IFDBlockTags
import cn.solarmoon.idyllic_food_diary.element.recipe.RollingRecipe
import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.RecipeSelectData
import cn.solarmoon.idyllic_food_diary.feature.util.ParticleUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.phys.collision.FreeCollisionBox
import cn.solarmoon.spark_core.api.phys.collision.FreeCollisionBoxRenderManager
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.api.util.DropUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.CrossbowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import java.util.function.Consumer
import java.util.stream.Stream

class RollingPinItem(
    tier: Tier = Tiers.WOOD,
    properties: Properties = Properties().attributes(createAttributes(Tiers.WOOD, 3, -2.4f)),
    toolComponentData: Tool = createToolProperties()
): SwordItem(tier, properties, toolComponentData) {

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int {
        return 78000
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player ?: return InteractionResult.PASS
        // 限制擀面触发的空间在正前方
        if (context.clickedPos != getStepPos(player, 2)) return InteractionResult.PASS
        return if (tryStartRolling(player, context.hand, context.clickedPos, context.level)) InteractionResult.CONSUME else InteractionResult.PASS
    }

    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration)

        if (livingEntity is Player) {
            val rollingContent = stack.getOrDefault(IFDDataComponents.ROLLING_CONTENT, RollingContent.EMPTY)
            val targetPos = rollingContent.targetPos
            // 限制擀面空间在正前方
            if (targetPos != getStepPos(livingEntity, 2)) livingEntity.stopUsingItem()
            // 不匹配一开始点击的方块坐标就重置
            if (!getPlayerPOVHitResult(level, livingEntity, ClipContext.Fluid.NONE).blockPos.equals(targetPos)) livingEntity.stopUsingItem()
            // 配方实现
            val tick = getUseDuration(stack, livingEntity) - remainingUseDuration
            @Suppress("UNCHECKED_CAST")
            val recipe = level.recipeManager.byKey(rollingContent.targetRecipeId).get() as RecipeHolder<RollingRecipe>
            if (tick > recipe.value.time) {
                val lastBlockState = level.getBlockState(targetPos)
                val stateSet = BlockUtil.inheritBlockWithAllState(lastBlockState, recipe.value().output.defaultBlockState())
                level.destroyBlock(targetPos, false)
                level.setBlock(targetPos, stateSet, 3)
                recipe.value.getRolledResults(livingEntity).forEach { DropUtil.summonDrop(it, level, targetPos) }
                livingEntity.stopUsingItem()
            }

            val sound = level.getBlockState(targetPos).getSoundType(level, targetPos, null).hitSound
            if (tick % 3 == 0) {
                ParticleUtil.rolling(targetPos, level)
                level.playSound(null, livingEntity.onPos, sound, SoundSource.BLOCKS)
            }
        }
    }

    override fun onEntitySwing(stack: ItemStack, entity: LivingEntity, hand: InteractionHand): Boolean {
        val level = entity.level()
        val box = entity.boundingBox.inflate(3.0).setMinY(entity.y)
        val debug = FreeCollisionBoxRenderManager("RollingStormFrom${entity.stringUUID}", FreeCollisionBox.of(box))
        debug.start()
        var flag = false
        val blocksFind = if (entity.isCrouching) BlockPos.betweenClosedStream(box) else if (entity is Player) Stream.of(getPlayerPOVHitResult(level, entity, ClipContext.Fluid.NONE).blockPos) else Stream.of<BlockPos>()
        blocksFind.forEach { pos ->
            val state = level.getBlockState(pos)
            var drop = mutableListOf(ItemStack.EMPTY)
            if (state.`is`(IFDBlockTags.ROLLABLE)) {
                if (state.`is`(Blocks.CAKE)) {
                    if (state.getValue(BlockStateProperties.BITES) != 0) return@forEach //防止刷蛋糕
                    else drop.add(ItemStack(Items.CAKE))
                }
                val move = Vec3(0.0, 0.4, 0.0)
                if (level is ServerLevel) {
                    if (drop.isEmpty()) drop = Block.getDrops(state, level, pos, null)
                    DropUtil.summonDrop(drop, level, pos, move)
                    level.destroyBlock(pos, false)
                    level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 1.0f, 0.5f)
                    stack.hurtAndBreak(1, entity, LivingEntity.getSlotForHand(hand))
                    level.sendParticles(ParticleTypes.SWEEP_ATTACK, pos.center.x, pos.center.y, pos.center.z, 1, 0.0, 0.0, 0.0, 0.0)
                }
                debug.setHit()
                flag = true
            }
        }
        if (flag) entity.swinging = true
        return flag
    }

    companion object {
        @JvmStatic
        fun getStepPos(livingEntity: LivingEntity, yOffset: Int): BlockPos {
            return BlockPos(livingEntity.onPos.x + livingEntity.direction.stepX, livingEntity.onPos.y + yOffset, livingEntity.onPos.z + livingEntity.direction.stepZ)
        }

        /**
         * 找出满足同输入的所有配方
         */
        @JvmStatic
        fun findRecipes(input: Block, level: Level): List<RecipeHolder<RollingRecipe>> {
            return level.recipeManager.getAllRecipesFor(IFDRecipes.ROLLING.type.get()).filter { it.value.input.test(ItemStack(input.asItem())) }.toList()
        }

        /**
         * 先找出所有同输入的配方，然后根据[selectIndex]选择具体哪一个配方
         *
         * 如果只是想通过此方法判断是否输入匹配，将[selectIndex]设为0即可
         */
        @JvmStatic
        fun findRecipe(input: Block, level: Level, selectIndex: Int): RecipeHolder<RollingRecipe>? {
            var sli = selectIndex
            val matchRecipes = findRecipes(input, level)
            if (sli !in 0 until matchRecipes.size ) sli = 0 // 序列矫正，如果超出可用列表范围就改为0防止错误
            return if (matchRecipes.isEmpty()) null else matchRecipes[sli]
        }

        /**
         * 如果目视方块输入匹配配方则开始擀面，并保存当前配方和指向的方块位置信息
         */
        @JvmStatic
        fun tryStartRolling(entity: LivingEntity, hand: InteractionHand, targetPos: BlockPos, level: Level): Boolean {
            val pin = entity.getItemInHand(hand)
            val input = level.getBlockState(targetPos).block
            findRecipe(input, level, pin.getOrDefault(IFDDataComponents.RECIPE_SELECTION, RecipeSelectData.EMPTY).getIndex(input))?.let {
                pin.set(IFDDataComponents.ROLLING_CONTENT, RollingContent(targetPos, it.id))
                entity.startUsingItem(hand)
                return true
            }
            return false
        }
    }


}