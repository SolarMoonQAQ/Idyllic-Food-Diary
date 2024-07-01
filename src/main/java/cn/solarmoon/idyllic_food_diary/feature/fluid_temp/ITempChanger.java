package cn.solarmoon.idyllic_food_diary.feature.fluid_temp;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.idyllic_food_diary.network.NETList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ITempChanger extends IContainerTile, ITankTile {

    /**
     * 单个变温剂所能变温的最大液体量
     */
    int MAX_CHANGE_AMOUNT_PER_CHANGER = 500;

    default BlockEntity tc() {
        return (BlockEntity) this;
    }

    /**
     * 应用温度改变剂效果
     */
    default boolean tryApplyThermochanger() {
        Level level = tc().getLevel();
        if (level == null || level.isClientSide) return false;
        BlockPos pos = tc().getBlockPos();
        FluidStack fluidStack = getTank().getFluid();
        // 有冷却剂且足量就把热液体急速冷却
        if (getCoolantsAmount() >= getThermochangerRequirement()) {
            if (Temp.shrink(fluidStack)) {
                int shrinkCount = getThermochangerRequirement();
                for (var stack : getAllCoolant()) {
                    if (!stack.isEmpty() && shrinkCount > 0) {
                        stack.shrink(1);
                        shrinkCount--;
                    }
                }
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1, 1.5F);
                IMPacks.CLIENT_PACK.getSender().pos(pos).send(NETList.PARTICLE_TEMP_COOL);
                tc().setChanged();
                return true;
            }
        }
        return false;
    }

    default List<ItemStack> getAllCoolant() {
        return ItemHandlerUtil.getStacks(getInventory()).stream().filter(stack -> stack.is(IMItemTags.COOLANT)).collect(Collectors.toList());
    }

    default int getCoolantsAmount() {
        int c = 0;
        for (var stack : getAllCoolant()) {
            c += stack.getCount();
        }
        return c;
    }

    default int getThermochangerRequirement() {
        int fluidAmount = getTank().getFluidAmount();
        int requirement = fluidAmount / MAX_CHANGE_AMOUNT_PER_CHANGER;
        return (fluidAmount % MAX_CHANGE_AMOUNT_PER_CHANGER == 0) ? requirement : requirement + 1;
    }

}
