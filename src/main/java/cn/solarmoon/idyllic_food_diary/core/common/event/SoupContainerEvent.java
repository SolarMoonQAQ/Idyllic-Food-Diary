package cn.solarmoon.idyllic_food_diary.core.common.event;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMBlockTags;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

public class SoupContainerEvent {

    @SubscribeEvent
    public void rightClickSoupContainer(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack heldStack = event.getItemStack();
        InteractionHand hand = event.getHand();

        if (serveSoup(player, level, pos, state, heldStack, hand)
                || putSoup(player, level, pos, state, heldStack, hand)) {
            event.setCanceled(true);
        }
    }

    private boolean serveSoup(Player player, Level level, BlockPos pos, BlockState state, ItemStack heldItem, InteractionHand hand) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (hand == InteractionHand.MAIN_HAND && state.is(IMBlockTags.SOUP_CONTAINER) && blockEntity != null) { //必须在主手才能用，不然会和取物逻辑有些混淆导致同时执行
            IFluidHandler tank = FluidUtil.getTank(blockEntity);
            Optional<SoupServingRecipe> recipeOptional = getCheckedRecipe(level, heldItem, tank, CheckSource.CONTAINER);
            IdyllicFoodDiary.DEBUG.send(recipeOptional.isPresent() ? recipeOptional.toString() : "null");
            if (recipeOptional.isPresent()) {
                SoupServingRecipe recipe = recipeOptional.get();

                if (!player.isCreative()) {
                    heldItem.shrink(1);
                    LevelSummonUtil.addItemToInventory(player, recipe.result().copy(), pos);
                }
                tank.drain(recipe.getAmountToServe(), IFluidHandler.FluidAction.EXECUTE);
                blockEntity.setChanged();
                player.swing(hand);
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                return true;
            }
        }
        return false;
    }

    private boolean putSoup(Player player, Level level, BlockPos pos, BlockState state, ItemStack heldItem, InteractionHand hand) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (hand == InteractionHand.MAIN_HAND && state.is(IMBlockTags.SOUP_CONTAINER) && blockEntity != null) { //必须在主手才能用，不然会和取物逻辑有些混淆导致同时执行
            IFluidHandler t = FluidUtil.getTank(blockEntity);
            Optional<SoupServingRecipe> recipeOptional = getCheckedRecipe(level, heldItem, t, CheckSource.FOOD);
            IdyllicFoodDiary.DEBUG.send(recipeOptional.isPresent() ? recipeOptional.toString() : "null");
            if (recipeOptional.isPresent()) {
                SoupServingRecipe recipe = recipeOptional.get();
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                    LevelSummonUtil.addItemToInventory(player, recipe.container().copy(), pos);
                }
                t.fill(recipe.fluidToServe().copy(), IFluidHandler.FluidAction.EXECUTE);
                blockEntity.setChanged();
                player.swing(hand);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                return true;
            }
        }
        return false;
    }

    private Optional<SoupServingRecipe> getCheckedRecipe(Level level, ItemStack heldItem, IFluidHandler tank, CheckSource source) {
        List<SoupServingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_SERVING.get());
        switch (source) {
            case CONTAINER -> {
                return recipes.stream().filter(recipe ->
                                heldItem.is(recipe.container().getItem())
                        && tank.getFluidInTank(0).containsFluid(recipe.fluidToServe())
                ).findFirst();
            }
            case FOOD -> {
                return recipes.stream().filter(recipe ->
                                heldItem.is(recipe.result().getItem())
                                        && FluidUtil.canStillPut(tank, recipe.fluidToServe())
                ).findFirst();
            }
            default -> throw new RuntimeException("Unknown Check Source.");
        }
    }

    private enum CheckSource {
        CONTAINER, FOOD
    }


}
