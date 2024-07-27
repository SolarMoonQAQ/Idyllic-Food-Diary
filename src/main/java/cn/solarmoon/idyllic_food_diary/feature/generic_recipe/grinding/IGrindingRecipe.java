package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grinding;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneInventory;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.fluid.IMultiTankTile;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

public interface IGrindingRecipe extends IContainerTile, IMultiTankTile<TileTank> {

    String GRINDING_TIME = "GrindingTime";
    String GRINDING_RECIPE_TIME = "GrindingRecipeTime";
    String FLOWING = "Flowing";

    default BlockEntity gr() {
        return (BlockEntity) this;
    }

    default boolean tryGrind() {
        // 无配方不保存时间，有配方则保存时间
        if (findGrindingRecipe().isEmpty()) {
            setGrindingRecipeTime(0);
            setGrindingTime(0);
            return false;
        }
        AnimTicker anim = AnimHelper.getMap(gr()).get("rotation");
        return findGrindingRecipe().map(recipe -> {
            if (anim.getTimer().getTiming()) {
                setGrindingTime(getGrindingTime() + 1);
                setGrindingRecipeTime(recipe.time());
                if (getGrindingTime() > recipe.time()) {
                    if (isRecipeSmooth()) {
                        getInventory().getStackInSlot(0).shrink(1);
                        ((MillstoneInventory)getInventory()).realInsert(1, recipe.result().copy(), false);
                        getTank(0).drain(recipe.inputFluid(), IFluidHandler.FluidAction.EXECUTE);
                        getTank(1).fill(recipe.outputFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
                        setGrindingTime(0);
                        setGrindingRecipeTime(0);
                    }
                }
                return true;
            }
            return false;
        }).orElse(false);
    }

    /**
     * @return 配方是否能顺利进行（1.配方匹配 2.拥有足够空间给所有配方产物）
     */
    default boolean isRecipeSmooth() {
        return findGrindingRecipe().map(recipe ->
                        getTank(1).fill(recipe.outputFluid(), IFluidHandler.FluidAction.SIMULATE) == recipe.outputFluid().getAmount()
                                && !((MillstoneInventory)getInventory()).realInsert(1, recipe.result().copy(), true).equals(recipe.result().copy(), false)
                )
                .orElse(false);
    }

    default Optional<IFluidHandler> getFluidReceiver() {
        BlockState state = gr().getBlockState();
        BlockPos pos = gr().getBlockPos();
        Direction side = state.getValue(IHorizontalFacingBlock.FACING);
        Level level = gr().getLevel();
        if (level == null) return Optional.empty();
        pos = pos.relative(side);
        // 前方不能有阻挡
        if (!level.getBlockState(pos).isAir()) return Optional.empty();
        pos = pos.below();
        // 这里写上限定方块条件
        return FluidUtil.getFluidHandler(level, pos, Direction.UP).resolve();
    }

    default void transferOfFluid() {
        if (getFluidReceiver().isEmpty()) setFlowing(false);
        getFluidReceiver().ifPresent(t -> {
            // 必须能传递液体才行
            IFluidHandler.FluidAction s = IFluidHandler.FluidAction.SIMULATE;
            if (t.fill(getTank(1).drain(1, s), s) == 1) {
                setFlowing(true);
                t.fill(getTank(1).drain(1, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
            } else setFlowing(false);
        });
    }

    default Optional<GrindingRecipe> findGrindingRecipe() {
        return findGrindingRecipe(getInventory().getStackInSlot(0)).filter(recipe ->
                getTank(0).drain(recipe.inputFluid(), IFluidHandler.FluidAction.SIMULATE).getAmount() == recipe.inputFluid().getAmount()
        );
    }

    /**
     * @return 判断物品能否放入用，别用这个检测配方，用上面那个
     */
    default Optional<GrindingRecipe> findGrindingRecipe(ItemStack match) {
        Level level = gr().getLevel();
        if (level == null) return Optional.empty();
        List<GrindingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.GRINDING.get());
        return recipes.stream().filter(recipe ->
                recipe.ingredient().test(match)
        ).findFirst();
    }

    int getGrindingTime();

    void setGrindingTime(int time);

    int getGrindingRecipeTime();

    void setGrindingRecipeTime(int time);

    void setFlowing(boolean flag);

    /**
     * @return 是否正在传输输出流体
     */
    boolean isFlowing();

}
