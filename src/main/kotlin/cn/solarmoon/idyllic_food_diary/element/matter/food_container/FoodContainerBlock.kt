package cn.solarmoon.idyllic_food_diary.element.matter.food_container

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.netwrok.ClientNetHandler
import cn.solarmoon.spark_core.api.blockentity.SyncedEntityBlock
import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.network.CommonNetData
import cn.solarmoon.spark_core.api.util.DropUtil
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.network.PacketDistributor

abstract class FoodContainerBlock(soundType: SoundType,
    properties: Properties = Properties.of()
        .sound(soundType)
        .strength(0.7F)
        .noOcclusion()
): SyncedEntityBlock(properties), IHorizontalFacingState {

    open val eatCount = 5

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val container = level.getBlockEntity(pos) as FoodContainerBlockEntity
        val food = container.lastItem
        if (player.canEat(food.getFoodProperties(player)?.canAlwaysEat() == true) && !food.isEmpty) {
            //计数装置
            val counting = player.getData(SparkAttachments.COUNTING_DEVICE)
            counting.setCount(counting.count + 1, pos)
            //吃的声音
            level.playSound(null, pos, food.eatingSound, SoundSource.PLAYERS)
            //吃的粒子效果
            if (!level.isClientSide) PacketDistributor.sendToAllPlayers(CommonNetData(itemStack = food, uuid = player.uuid, message = ClientNetHandler.EAT_PARTICLE))
            if (counting.count >= eatCount) {
                //吃掉！
                val give = food.finishUsingItem(level, player)
                if (!player.isCreative) {
                    if (!give.isEmpty) DropUtil.summonDrop(give, level, pos.center)
                    // 保证能够推出食用后的物品（比如碗装食物吃掉后能够推出碗）
                }
                else {
                    food.shrink(1); //创造模式也消耗食物
                }
                //重置计数
                counting.setCount(0)
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return InteractionResult.PASS
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val container = level.getBlockEntity(pos) as FoodContainerBlockEntity
        val inv = container.inventory
        if (ItemStackHandlerHelper.putItem(inv, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS, 0.5f, 1f);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (player.isCrouching && ItemStackHandlerHelper.takeItem(inv, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS, 0.5f, 1f);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun getDrops(state: BlockState, builder: LootParams.Builder): List<ItemStack> {
        val origin = mutableListOf<ItemStack>()
        val blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) ?: return origin
        val level = builder.level
        val stack = ItemStack(this)
        if (state.hasProperty(IBedPartState.PART) && state.getValue(IBedPartState.PART) == BedPart.HEAD) return origin
        blockEntity.saveToItem(stack, level.registryAccess())
        origin.add(stack)
        return origin
    }

}