package cn.solarmoon.idyllic_food_diary.feature.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.Random

object ParticleUtil {

    @JvmStatic
    fun rolling(pos: BlockPos, level: Level) {
        rolling(pos, level, level.getBlockState(pos).block.asItem().defaultInstance, 0.0, 5)
    }

    @JvmStatic
    fun rolling(pos: BlockPos, level: Level, item: ItemStack, yOffset: Double, count: Int) {
        val particle = ItemParticleOption(ParticleTypes.ITEM, item)
        val blockState = level.getBlockState(pos)

        val minX = blockState.getShape(level, pos).min(Direction.Axis.X)
        val minY = blockState.getShape(level, pos).min(Direction.Axis.Y)
        val minZ = blockState.getShape(level, pos).min(Direction.Axis.Z)
        val maxX = blockState.getShape(level, pos).max(Direction.Axis.X)
        val maxY = blockState.getShape(level, pos).max(Direction.Axis.Y)
        val maxZ = blockState.getShape(level, pos).max(Direction.Axis.Z)

        for(i in 0 until  count) {
            val random = Random()
            val posX = pos.x + minX + (maxX - minX) * random.nextDouble()
            val posY = if (yOffset > 0) pos.y + yOffset else pos.y + minY + (maxY - minY) * random.nextDouble()
            val posZ = pos.z + minZ + (maxZ - minZ) * random.nextDouble()

            val velocityX = (posX - (pos.x + 0.5)) * 0.2
            val velocityY = (posY - (pos.y + maxY)) * 0.1
            val velocityZ = (posZ - (pos.z + 0.5)) * 0.2

            level.addParticle(particle, posX, posY, posZ, velocityX, velocityY, velocityZ)
        }
    }

}