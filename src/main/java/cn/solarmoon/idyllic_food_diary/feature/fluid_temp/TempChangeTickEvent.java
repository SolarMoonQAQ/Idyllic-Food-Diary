package cn.solarmoon.idyllic_food_diary.feature.fluid_temp;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.HashSet;
import java.util.Set;

public class TempChangeTickEvent {

    @SubscribeEvent
    public void levelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (level instanceof ServerLevel sl) {
            Set<ChunkPos> tickedChunks = new HashSet<>(); // 使用set保证同一区块只tick一次
            sl.getEntities().getAll().forEach(entity -> {
                BlockPos pos = entity.getOnPos();
                ChunkPos chunkPos = new ChunkPos(pos);
                if (!tickedChunks.contains(chunkPos)) {
                    sl.getChunkAt(pos).getBlockEntities().forEach((p, be) -> {
                        be.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(tank -> {
                            FluidStack fluidStack = tank.getFluidInTank(0);
                            boolean onHeat = be instanceof IHeatable heatable && heatable.isOnHeatSource();
                            boolean mtHeat = onHeat && Temp.get(fluidStack).isSame(Temp.HOT); // 如果在热源上就保持热
                            if (!mtHeat && Temp.tick(tank.getFluidInTank(0))) {
                                be.setChanged();
                            }
                        });
                    });
                    tickedChunks.add(chunkPos);
                }
            });
        }
    }


    public static void tickInInventory(ItemStack stack) {
        FluidUtil.getFluidHandler(stack).ifPresent(tank -> {
            FluidStack fluidStack = tank.getFluidInTank(0);
            Temp.tick(fluidStack);
        });
    }

}
