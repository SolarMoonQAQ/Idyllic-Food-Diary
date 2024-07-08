package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.ingredient_handling;

import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public interface IIngredientHandlingRecipe extends ISpiceable {

    default BlockEntity selfI() {
        return (BlockEntity) this;
    }

    /**
     * 尝试当玩家拿着对应道具右键时转化输出
     */
    default boolean trOutputResult(ItemStack heldItem, Player player, BlockPos pos, Level level) {
        if (findHandleRecipe().isPresent()) {
            IngredientHandlingRecipe recipe = findHandleRecipe().get();
            if (ContainerHelper.test(recipe.container(), heldItem) && withTrueSpices(recipe.withSpices(), true)) {
                ItemHandlerUtil.clearInv(getInventory(), selfI());
                ItemStack result = recipe.result().copy();
                ItemHandlerUtil.insertItem(getInventory(), result);
                ContainerHelper.setContainer(result, heldItem);
                if (!player.isCreative()) heldItem.shrink(1);
                Vec3 center = pos.getCenter();
                RandomSource randomSource = player.getRandom();
                for (int i = 0; i < randomSource.nextInt(2, 5); i ++) {
                    level.addParticle(ParticleTypes.END_ROD, pos.getX() + randomSource.nextDouble(), center.y - 0.5, pos.getZ() + randomSource.nextDouble(), 0, 0.1, 0);
                }
                level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.get(), SoundSource.BLOCKS, 1, 2);
                selfI().setChanged();
                return true;
            }
        }
        return false;
    }

    default boolean hasOutput() {
        return findHandleRecipe().isPresent() && withTrueSpices(findHandleRecipe().get().withSpices(), true);
    }

    default Optional<IngredientHandlingRecipe> findHandleRecipe() {
        Level level = selfI().getLevel();
        if (level == null) return Optional.empty();
        List<IngredientHandlingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.INGREDIENT_HANDLING.get());
        for (IngredientHandlingRecipe recipe :recipes) {
            boolean pass = true;
            if (ItemHandlerUtil.getStacks(getInventory()).size() == recipe.ingredients().size()) {
                if (recipe.isInOrder()) {
                    for (int i = 0; i < recipe.ingredients().size(); i++) {
                        if (!recipe.ingredients().get(i).test(getInventory().getStackInSlot(i))) {
                            pass = false;
                            break;
                        }
                    }
                } else {
                    boolean[] matched = new boolean[recipe.ingredients().size()];
                    for (int i = 0; i < ItemHandlerUtil.getStacks(getInventory()).size(); i++) {
                        boolean found = false;
                        for (int j = 0; j < recipe.ingredients().size(); j++) {
                            if (!matched[j] && recipe.ingredients().get(j).test(getInventory().getStackInSlot(i))) {
                                matched[j] = true;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            pass = false;
                            break;
                        }
                    }
                }
                if (pass) {
                    return Optional.of(recipe);
                }
            }
        }
        return Optional.empty();
    }

}
