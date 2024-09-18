package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.idyllic_food_diary.registry.common.IFDParticles
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.util.DropUtil
import cn.solarmoon.spark_core.api.util.HitResultUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class SteamerBlock: CookwareBlock(
    Properties.of()
    .sound(SoundType.BAMBOO)
    .strength(2f)
    .noOcclusion()
) {

    override fun useItemOnThis(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val steamer = level.getBlockEntity(pos) as SteamerBlockEntity
        var hitLayer = ((hitResult.location.y - pos.y) * 16 / (16 / steamer.maxLayer)).toInt()
        if (hitResult.direction == Direction.UP) hitLayer--

        // 堆叠蒸笼
        if (heldItem.`is`(asItem())) {
            if (steamer.addLayerFromItem(heldItem, level.registryAccess())) {
                if (!player.isCreative) heldItem.shrink(1)
                level.playSound(null, pos, getSoundType(state, level, pos, null).placeSound, SoundSource.BLOCKS)
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }

        // 放入盖子
        if (heldItem.`is`(IFDItems.STEAMER_LID) && !steamer.hasLid && hitResult.direction == Direction.UP) {
            steamer.lid = heldItem.copyWithCount(1)
            if (!player.isCreative) heldItem.shrink(1)
            steamer.setChanged()
            level.playSound(null, pos, getSoundType(state, level, pos, null).placeSound, SoundSource.BLOCKS)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }

        // 起开盖子
        if (steamer.hasLid && hitResult.direction == Direction.UP && heldItem.isEmpty) {
            if (!player.isCreative) DropUtil.addItemToInventory(player, steamer.lid)
            steamer.clearLid()
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.BLOCKS)
            steamer.setChanged()
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }

        // 精准与各层进行物品交互
        hitLayer.takeIf { it < steamer.presentLayer }?.run {
            val hitInv = steamer.inventories[hitLayer]
            if (ItemStackHandlerHelper.putItem(hitInv, player, hand, 1)) {
                level.playSound(null, pos, getSoundType(state, level, pos, null).hitSound, SoundSource.BLOCKS)
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            } else if (ItemStackHandlerHelper.takeItem(hitInv, player, hand, 1)) {
                level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS)
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun attack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        val steamer = level.getBlockEntity(pos) as SteamerBlockEntity
        // 空手送一个新的，拿着蒸笼直接往上叠
        if (player.isCrouching) steamer.takeLastLayerToItem(player.mainHandItem, player, level.registryAccess())
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        super.tick(level, pos, state, blockEntity)
        val steamer = blockEntity as SteamerBlockEntity
        steamer.steaming.tryWork()
        steamer.tryPreventLidSet()
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        val steamer = level.getBlockEntity(pos) as SteamerBlockEntity
        if (steamer.getBase() != null && steamer.isTop) {
            val xInRange = pos.x + 2.0 / 16f + random.nextFloat() * 12 / 16
            val h = pos.y + 4.0 / 16f * steamer.presentLayer - 1 / 16f
            val zInRange = pos.z + 2.0 / 16f + random.nextFloat() * 12 / 16
            if (steamer.getBase()?.isValidForSteamer() == true) {
                // 有盖子降低蒸汽外流几率
                if (random.nextInt(10) < (if (steamer.hasLid) 3 else 10)) {
                    level.addAlwaysVisibleParticle(IFDParticles.CRASHLESS_CLOUD.get(), xInRange, h, zInRange, 0.0, 0.1, 0.0)
                }
            }
        }
    }

    override fun getShapeThis(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val steamer = level.getBlockEntity(pos)
        if (steamer is SteamerBlockEntity) {
            return box(0.0, 0.0, 0.0, 16.0, steamer.presentLayer * 16.0 / steamer.maxLayer, 16.0)
        }
        return Shapes.block()
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.STEAMER.get()
    }

}