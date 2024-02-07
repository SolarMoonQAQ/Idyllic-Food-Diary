package cn.solarmoon.immersive_delight.common.item;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.common.item.IOptionalRecipeItem;
import cn.solarmoon.immersive_delight.api.util.LevelSummonUtil;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.compat.jade.impl.IJadeRecipeProgressItem;
import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import cn.solarmoon.immersive_delight.util.AnimController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.solarmoon.immersive_delight.client.particle.vanilla.Rolling.rolling;
import static cn.solarmoon.immersive_delight.client.particle.vanilla.Sweep.sweep;


public class RollingPinItem extends SwordItem implements IOptionalRecipeItem<RollingPinRecipe>, IJadeRecipeProgressItem {

    private boolean recipeMatches;
    private final List<RollingPinRecipe> matchingRecipes;
    /**
     * 涵盖了所有匹配配方的单个可输出物品的集合
     */
    private final List<ItemStack> optionalOutputs;
    public int recipeTime;
    private BlockPos equalBlockPos;
    private ItemStack equalOutput;

    /**
     * 属性与木剑类似
     */
    public RollingPinItem() {
        super(Tiers.WOOD,3, -2.4f, new Item.Properties().durability(512));
        matchingRecipes = new ArrayList<>();
        optionalOutputs = new ArrayList<>();
    }

    /**
     * 动态的决定使用时间
     */
    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return recipeTime;
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

        if (heldStack.getItem() instanceof RollingPinItem pin && player != null) {
            pin.equalBlockPos = context.getClickedPos();
            BlockPos exceptPos = new BlockPos(player.getOnPos().getX() + player.getDirection().getStepX(), player.getOnPos().getY() + 1, player.getOnPos().getZ() + player.getDirection().getStepZ());
            //限制擀面空间
            if (equalBlockPos.equals(exceptPos) || equalBlockPos.equals(exceptPos.above())) {
                if (recipeMatches()) {
                    pin.recipeTime = getSelectedRecipe(player).getTime();
                    player.startUsingItem(hand);
                    pin.equalOutput = pin.getSelectedOutput(player);
                } else {
                    pin.recipeTime = 0;
                }
            }
        }
        return InteractionResult.FAIL;
    }


    private int tickCounter = 0;
    /**
     * 使用时持续播放动画<br/>
     * 这一步用于在使用期间，如果发生视角转移（也就是目视方块配方不匹配擀面配方了）等情况，就停止使用<br/>
     * 同时在使用期间，每3tick就播放一次擀面声音（擀面+被擀方块的混合声）
     */
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int i) {
        if (stack.getItem() instanceof RollingPinItem pin) {
            new AnimController(entity).playAnim(20, "waving");
            //不匹配配方或是在此期间更换配方就停用
            if (!pin.recipeMatches() || !pin.getSelectedOutput(entity).is(pin.equalOutput.getItem())) {
                new AnimController(entity).stopAnim(10);
                entity.stopUsingItem();
            }
            if (level.isClientSide) {
                rolling(pin.equalBlockPos, level);
            } else pin.tickCounter++;
            if (pin.tickCounter >= 3) {
                BlockState state = level.getBlockState(pin.equalBlockPos);
                SoundEvent breakSound = state.getSoundType().getBreakSound();
                level.playSound(null, pin.equalBlockPos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pin.equalBlockPos, breakSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                pin.tickCounter = 0;
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
            if (pin.recipeTime > 0) {

                BlockPos pos = pin.equalBlockPos;

                RollingPinRecipe recipe = pin.getSelectedRecipe(entity);
                if (recipe.getOutput() != null) {
                    BlockState state = Block.byItem(pin.equalOutput.getItem()).defaultBlockState();
                    level.destroyBlock(pos, false);
                    level.setBlock(pos, state, 3);
                }

                List<ItemStack> results = recipe.rollResults(player);
                if (!results.isEmpty()) {
                    //根据碰撞箱高度决定掉落物生成高度
                    double maxY = level.getBlockState(pos).getShape(level, pos).max(Direction.Axis.Y);
                    Vec3 vec3 = new Vec3(pos.getX() + 0.5, pos.getY() + maxY / 2, pos.getZ() + 0.5);
                    LevelSummonUtil.summonDrop(results, level, vec3);
                }

                //减少耐久度
                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(e.getUsedItemHand()));
                pin.recipeTime = 0;
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
            if (state.is(IMBlockTags.CAN_BE_ROLLED)) {
                //防止刷蛋糕
                if (state.is(Blocks.CAKE) && state.getValue(BlockStateProperties.BITES) != 0) return false;
                if (level instanceof ServerLevel sl) {
                    Vec3 move = new Vec3(0, 0.4, 0);
                    LevelSummonUtil.summonDrop(Block.getDrops(state, sl, pos, null), level, pos, move);
                }
                level.destroyBlock(pos, false);
                level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 1.0f, 0.5f);
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                if (level.isClientSide) sweep();
            } else if (player.isCrouching()) {
                //0就根据配置文件来，其它的就是独自设置
                int radius = 2;
                // 遍历玩家周围半径的方块
                BlockPos playerPos = entity.blockPosition();
                for(int x = -radius; x <= radius; x++) {
                    for(int y = 0; y <= radius; y++) {
                        for(int z = -radius; z <= radius; z++) {
                            BlockPos pPos = playerPos.offset(x, y, z);
                            BlockState tState = level.getBlockState(pPos);
                            TagKey<Block> tag = BlockTags.create(new ResourceLocation(ImmersiveDelight.MOD_ID, "can_be_rolled"));
                            if(tState.is(tag)) {
                                if(level.isClientSide) level.addParticle(ParticleTypes.SWEEP_ATTACK, pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5, 0,0,0);
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
    public RecipeType<RollingPinRecipe> getRecipeType() {
        return IMRecipes.ROLLING_RECIPE.get();
    }

    @Override
    public boolean recipeCheckAndUpdate(RollingPinRecipe recipe, ItemStack hitStack) {
        return recipe.getInput().test(hitStack);
    }

    @Override
    public boolean recipeMatches() {
        return recipeMatches;
    }

    @Override
    public void setRecipeMatch(boolean recipeMatches) {
        this.recipeMatches = recipeMatches;
    }

    @Override
    public List<ItemStack> getOptionalOutputs() {
        //先重置，否则会一直累加
        optionalOutputs.clear();
        for (RollingPinRecipe recipe : getMatchingRecipes()) {
            if(recipe.getOutput() != null) {
                Ingredient output = recipe.getOutput();
                optionalOutputs.addAll(Arrays.asList(output.getItems()));
            } else {
                ItemStack put = recipe.getResults().get(0);
                optionalOutputs.add(put);
            }
        }
        return optionalOutputs;
    }

    @Override
    public List<RollingPinRecipe> getMatchingRecipes() {
        return matchingRecipes;
    }

    @Override
    public int getRecipeTime() {
        return recipeTime;
    }
}

