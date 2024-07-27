package cn.solarmoon.idyllic_food_diary.api

import cn.solarmoon.solarmoon_core.api.phys.SMath
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler

object AnimHelper {

    object Fluid {

        /**
         * 在anim所拥有的各个map中，所有和此处基本液体相关的内容所用到的标识名
         */
        const val IDENTIFIER = "Fluid"

        /**
         * 默认当液体储罐改变时开始播放
         * @param fluidStack 如果动画正在进行仍然调用了这个方法（也就是液体连续改变），立刻结束动画并设定当前液体，防止动画停止或断断续续
         */
        @JvmStatic
        fun startFluidAnim(be: BlockEntity, fluidStack: FluidStack) {
            val anim = getMap(be)[IDENTIFIER]!!
            val timer = anim.timer
            if (timer.timing) {
                onFluidAnimStop(be, fluidStack)
                return
            }
            timer.maxTime = 10f
            timer.start()
        }

        /**
         * 自行在tick调用，检查当液体动画结束时存入当前液体<br>
         * （不用Timer的onStop是因为会闪一下）
         */
        @JvmStatic
        fun onFluidAnimStop(be: BlockEntity, fluidStack: FluidStack) {
            val anim = getMap(be)[IDENTIFIER]!!
            val timer = anim.timer
            if (timer.time >= timer.maxTime) {
                anim.fixedTags[IDENTIFIER] = fluidStack.writeToNBT(CompoundTag())
            }
        }

        /**
         * 渲染指定液体及其变化后的上升下降过渡动画<br>
         * 要想正确使用此方法，必须在方块实体的tick中调用 [onFluidAnimStop] 方法
         * @param height 液体最大高度
         */
        @JvmStatic
        @OnlyIn(Dist.CLIENT)
        fun renderAnimatedFluid(
            be: BlockEntity,
            side: Direction?,
            width: Float,
            height: Float,
            yOffset: Double,
            partialTicks: Float,
            poseStack: PoseStack,
            buffer: MultiBufferSource,
            light: Int
        ) {
            val anim = getMap(be)[IDENTIFIER]!!
            be.getCapability(ForgeCapabilities.FLUID_HANDLER, side).ifPresent { tank ->
                val lastFluid = FluidStack.loadFluidStackFromNBT(anim.fixedTags[IDENTIFIER])
                val presentFluid = tank.getFluidInTank(0)
                val renderFluid = presentFluid.takeIf { !it.isEmpty } ?: lastFluid
                val color = TextureRenderUtil.getColor(renderFluid)
                val fluidAttributes = IClientFluidTypeExtensions.of(renderFluid.fluid)
                val spriteLocation = fluidAttributes.getStillTexture(renderFluid)

                val presentScale = lastFluid.amount / tank.getTankCapacity(0).toFloat()
                val targetScale = FluidHandlerUtil.getScale(tank)
                val uMax = (width * 16).toInt()
                val vMax = (width * 16).toInt() // 对于牛奶而言这个无法动态调整？
                val presentH = presentScale * height
                val targetH = targetScale * height
                val hDifference = targetH - presentH
                val realPartialTicks = partialTicks.takeIf { anim.timer.timing } ?: 0f
                val progress = anim.timer.getProgress(realPartialTicks).takeIf { hDifference != 0f } ?: 1f // 不加条件会闪一下
                val realH = presentH + SMath.smoothInterpolation(progress, 0f, hDifference, 1.4f)

                poseStack.pushPose()
                poseStack.translate(0.0, yOffset, 0.0)
                spriteLocation?.let { TextureRenderUtil.render(it, 0, 0, uMax, vMax, width, realH, color, 1f, 0, poseStack, buffer, light) }
                poseStack.popPose()
            }
        }

        /**
         * 按容器液体比例渲染静态液体
         */
        @JvmStatic
        @OnlyIn(Dist.CLIENT)
        fun renderStaticFluid(
            width: Float,
            height: Float,
            yOffset: Float,
            tank: IFluidHandler,
            poseStack: PoseStack,
            buffer: MultiBufferSource?,
            light: Int
        ) {
            poseStack.pushPose()
            poseStack.translate(0f, yOffset, 0f)
            val fluidStack = tank.getFluidInTank(0)
            val targetColor = TextureRenderUtil.getColor(fluidStack)
            val fluid = fluidStack.fluid
            val fluidAttributes = IClientFluidTypeExtensions.of(fluid)
            val spriteLocation = fluidAttributes.getStillTexture(fluidStack)
            val h = FluidHandlerUtil.getScale(tank) * height
            if (spriteLocation != null) {
                TextureRenderUtil.render(
                    spriteLocation,
                    0, 0, (width * 16).toInt(), (width * 16).toInt(), width, h,
                    targetColor, 1f, 0, poseStack, buffer, light
                )
            }
            poseStack.popPose()
        }

    }

    /**
     * 为方块实体创建自定义名称的动画计时器<br>
     * 可以重复调用，会在已有的基础上添加
     */
    @JvmStatic
    fun createMap(be: BlockEntity, vararg nameList: String) {
        val map = getMap(be)
        nameList.forEachIndexed { _, name ->
            map[name] = AnimTicker()
        }
    }

    /**
     * 获取方块实体自带的动画map
     */
    @JvmStatic
    fun getMap(be: BlockEntity): MutableMap<String, AnimTicker> {
        return (be as IBlockEntityAnimation).animTickers
    }

}