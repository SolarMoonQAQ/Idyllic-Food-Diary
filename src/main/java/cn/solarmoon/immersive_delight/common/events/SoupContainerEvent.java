package cn.solarmoon.immersive_delight.common.events;

import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.util.FluidHelper;
import cn.solarmoon.immersive_delight.data.fluid_foods.serializer.FluidFood;
import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import cn.solarmoon.immersive_delight.util.LevelSummonHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SoupContainerEvent {

    @SubscribeEvent
    public void getFoodFromFluid(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack heldStack = event.getItemStack();
        InteractionHand hand = event.getHand();

        if (state.is(IMBlockTags.SOUP_CONTAINER)) {
            if (blockEntity instanceof BaseTankBlockEntity t) {
                FluidStack fluidStackInTank = t.tank.getFluid();
                Fluid fluid = fluidStackInTank.getFluid();
                //下面这一行已经相当于检查了液体类型是否匹配
                FluidFood fluidFood = FluidFood.getByFluid(fluid);
                if(fluidFood == null) return;
                int amount = fluidFood.fluidAmount;
                ItemStack food = fluidFood.getFood().getDefaultInstance();
                Item container = fluidFood.getContainer();

                //保证容量充足，并且液体符合配方液体
                if (fluidStackInTank.getAmount() >= amount && container.equals(heldStack.getItem())) {
                    if (!player.isCreative()) {
                        LevelSummonHelper.addItemToInventoryPerfectly(player, food, pos, heldStack, hand);
                        heldStack.shrink(1);
                    }
                    t.tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                    player.swing(hand);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void putFluidFromFood(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack heldStack = event.getItemStack();
        InteractionHand hand = event.getHand();

        if (state.is(IMBlockTags.SOUP_CONTAINER)) {
            if (blockEntity instanceof BaseTankBlockEntity t) {
                FluidFood fluidFood = FluidFood.getByFood(heldStack.getItem());
                if (fluidFood == null) return;
                FluidStack put = new FluidStack(fluidFood.getFluid(), fluidFood.fluidAmount);

                if (fluidFood.getFood().equals(heldStack.getItem()) && FluidHelper.canStillPut(t.tank, put)) {
                    if (!player.isCreative()) {
                        ItemStack container = fluidFood.getContainer().getDefaultInstance();
                        LevelSummonHelper.addItemToInventoryPerfectly(player, container, heldStack, hand);
                        heldStack.shrink(1);
                    }
                    t.tank.fill(put, IFluidHandler.FluidAction.EXECUTE);
                    player.swing(hand);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                    event.setCanceled(true);
                }
            }
        }
    }

}
