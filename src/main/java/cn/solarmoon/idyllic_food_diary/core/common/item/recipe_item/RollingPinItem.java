package cn.solarmoon.idyllic_food_diary.core.common.item.recipe_item;

import cn.solarmoon.idyllic_food_diary.api.util.AnimController;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.RollingRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMBlockTags;
import cn.solarmoon.solarmoon_core.api.common.item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.api.common.item.iutor.ITimeRecipeItem;
import cn.solarmoon.solarmoon_core.api.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cn.solarmoon.idyllic_food_diary.api.util.ParticleSpawner.rolling;


public class RollingPinItem extends SwordItem implements IOptionalRecipeItem<RollingRecipe> {

    /**
     * 属性与木剑类似
     */
    public RollingPinItem() {
        super(Tiers.WOOD,3, -2.4f, new Item.Properties().durability(512));
    }

    /**
     * 动态的决定使用时间
     */
    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return getRecipeTime(stack);
    }

    /**
     * 擀面杖只能用于擀面配方中已有的方块<br/>
     * 这一步用于决定擀面杖是否开始使用并激活动作<br/>
     * 并从配方中读取相应的time值
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack heldStack = context.getItemInHand();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if (player != null) {
            setEqualBlockPos(heldStack, context.getClickedPos());
            BlockPos exceptPos = new BlockPos(player.getOnPos().getX() + player.getDirection().getStepX(), player.getOnPos().getY() + 1, player.getOnPos().getZ() + player.getDirection().getStepZ());
            //限制擀面空间
            if (getEqualBlockPos(heldStack).equals(exceptPos) || getEqualBlockPos(heldStack).equals(exceptPos.above())) {
                Optional<RollingRecipe> recipeOp = getSelectedRecipe(heldStack, player);
                if (recipeOp.isPresent()) {
                    RollingRecipe recipe = recipeOp.get();
                    setRecipeTime(heldStack, recipe.time());
                    player.startUsingItem(hand);
                } else setRecipeTime(heldStack, 0);
            }
        }
        return InteractionResult.FAIL;
    }

    public Optional<RollingRecipe> getSelectedRecipe(ItemStack stack, Player player) {
        return !this.getMatchingRecipes(player).isEmpty() ? Optional.ofNullable(this.getMatchingRecipes(player).get(this.getHitBlockRecipeIndex(stack, player))) : Optional.empty();
    }

    public static void setRecipeTime(ItemStack pin, int recipeTime) {
        pin.getOrCreateTag().putInt("RecipeTime", recipeTime);
    }

    public static void setEqualBlockPos(ItemStack pin, BlockPos pos) {
        TagUtil.putPos(pin.getOrCreateTag(), pos);
    }

    public static int getRecipeTime(ItemStack pin) {
        return pin.getOrCreateTag().getInt("RecipeTime");
    }

    public static BlockPos getEqualBlockPos(ItemStack pin) {
        return TagUtil.getBlockPos(pin.getOrCreateTag());
    }

    private int tickCounter = 0;
    /**
     * 使用时持续播放动画<br/>
     * 这一步用于在使用期间，如果发生视角转移（也就是目视方块配方不匹配擀面配方了）等情况，就停止使用<br/>
     * 同时在使用期间，每3tick就播放一次擀面声音（擀面+被擀方块的混合声）
     */
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int i) {
        if (entity instanceof Player player) {
            new AnimController(entity).playAnim(20, "waving");
            //不匹配配方或是在此期间更换配方就停用
            if (getMatchingRecipes(player).isEmpty()) {
                new AnimController(entity).stopAnim(10);
                entity.stopUsingItem();
            }
            if (level.isClientSide) {
                rolling(getEqualBlockPos(stack), level);
            } else tickCounter++;
            if (tickCounter >= 3) {
                BlockState state = level.getBlockState(getEqualBlockPos(stack));
                SoundEvent breakSound = state.getSoundType().getBreakSound();
                level.playSound(null, getEqualBlockPos(stack), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, getEqualBlockPos(stack), breakSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                tickCounter = 0;
            }
        }
    }

    /**
     * 使用成功结束后，输出对应配方的所有输出，并减少耐久度
     */
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        new AnimController(entity).stopAnim(10);
        entity.stopUsingItem();
        if (stack.getItem() instanceof RollingPinItem pin && entity instanceof Player player) {
            if (getRecipeTime(stack) > 0) {

                BlockPos pos = getEqualBlockPos(stack);

                Optional<RollingRecipe> recipeOp = pin.getSelectedRecipe(stack, player);
                if (recipeOp.isPresent()) {
                    RollingRecipe recipe = recipeOp.get();
                    BlockState originState = level.getBlockState(pos);
                    BlockState state = recipe.output().defaultBlockState();
                    level.destroyBlock(pos, false);
                    BlockUtil.replaceBlockWithAllState(originState, state, level, pos);

                    List<ItemStack> results = recipe.getRolledResults(player);
                    if (!results.isEmpty()) {
                        //根据碰撞箱高度决定掉落物生成高度
                        double maxY = level.getBlockState(pos).getShape(level, pos).max(Direction.Axis.Y);
                        Vec3 vec3 = new Vec3(pos.getX() + 0.5, pos.getY() + maxY / 2, pos.getZ() + 0.5);
                        LevelSummonUtil.summonDrop(results, level, vec3);
                    }
                }

                //减少耐久度
                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(e.getUsedItemHand()));
                setRecipeTime(stack, 0);
            }
        }
        return stack;
    }

    /**
     * 主动松开右键也能结束使用
     */
    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int p_41415_) {
        new AnimController(entity).stopAnim(10);
        entity.stopUsingItem();
    }

    /**
     * 防止其他情况的下未使用不结束动作（如切换物品）
     */
    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        new AnimController(entity).stopAnim(10);
    }

    /**
     * 擀面风暴！
     */
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        Level level = entity.level();
        if (entity instanceof Player player) {
            BlockPos pos = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE).getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(IMBlockTags.ROLLABLE)) {
                //防止刷蛋糕
                if (state.is(Blocks.CAKE) && state.getValue(BlockStateProperties.BITES) != 0) return false;
                if (level instanceof ServerLevel sl) {
                    Vec3 move = new Vec3(0, 0.4, 0);
                    LevelSummonUtil.summonDrop(Block.getDrops(state, sl, pos, null), level, pos, move);
                }
                level.destroyBlock(pos, false);
                level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 1.0f, 0.5f);
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                CommonParticleSpawner.sweep(player, level);
            } else if (player.isCrouching()) {
                int radius = 2;
                // 遍历玩家周围半径的方块
                BlockPos playerPos = entity.blockPosition();
                for(int x = -radius; x <= radius; x++) {
                    for(int y = 0; y <= radius; y++) {
                        for(int z = -radius; z <= radius; z++) {
                            BlockPos pPos = playerPos.offset(x, y, z);
                            BlockState tState = level.getBlockState(pPos);
                            if(tState.is(IMBlockTags.ROLLABLE)) {
                                level.addParticle(ParticleTypes.SWEEP_ATTACK, pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5, 0,0,0);
                                if (level instanceof ServerLevel sl) {
                                    Vec3 move = new Vec3(0, 0.4, 0);
                                    LevelSummonUtil.summonDrop(Block.getDrops(tState, sl, pPos, null), level, pPos, move);
                                }
                                level.destroyBlock(pPos, false);
                                for(int i = 0; i < 5; i++) {
                                    level.playSound(null, pPos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 2.0f, (1.1f- (float) i /5));
                                }
                                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(e.getUsedItemHand()));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 设置擀面杖的修复物为任意原木类
     */
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        Ingredient logs = Ingredient.of(ItemTags.LOGS);
        return logs.test(repair);
    }

    @Override
    public RecipeType<RollingRecipe> getRecipeType() {
        return IMRecipes.ROLLING.get();
    }

    @Override
    public boolean recipeMatches(RollingRecipe recipe, BlockState state, Level level, BlockHitResult blockHitResult, Player player) {
        return recipe.input().test(state.getCloneItemStack(blockHitResult, level, blockHitResult.getBlockPos(), player));
    }

    @Override
    public List<ItemStack> getItemsOnGui(Player player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (var recipe : getMatchingRecipes(player)) {
            if (recipe.hasBlockOutput()) {
                stacks.add(new ItemStack(recipe.output()));
            } else stacks.add(recipe.getResults().get(0));
        }
        return stacks;
    }

}

