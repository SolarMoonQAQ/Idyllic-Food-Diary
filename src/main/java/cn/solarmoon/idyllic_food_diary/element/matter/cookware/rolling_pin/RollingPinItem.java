package cn.solarmoon.idyllic_food_diary.element.matter.cookware.rolling_pin;

import cn.solarmoon.idyllic_food_diary.data.IMBlockTags;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.rolling.RollingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.api.util.CommonParticleSpawner;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import cn.solarmoon.solarmoon_core.api.util.TagUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
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
import net.minecraft.world.entity.HumanoidArm;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static cn.solarmoon.idyllic_food_diary.feature.basic_feature.ParticleSpawner.rolling;


public class RollingPinItem extends SwordItem implements IOptionalRecipeItem<RollingRecipe> {

    /**
     * 属性与木剑类似
     */
    public RollingPinItem() {
        super(Tiers.WOOD,3, -2.4f, new Item.Properties().durability(512));
    }

    public RollingPinItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
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

        if (player != null && hand == InteractionHand.MAIN_HAND) {
            setEqualBlockPos(heldStack, context.getClickedPos());
            BlockPos exceptPos = new BlockPos(player.getOnPos().getX() + player.getDirection().getStepX(), player.getOnPos().getY() + 1, player.getOnPos().getZ() + player.getDirection().getStepZ());
            //限制擀面空间
            if (getEqualBlockPos(heldStack).equals(exceptPos.above()) && getEqualBlockPos(heldStack).equals(exceptPos) && !player.isCrouching()) {
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
            //不匹配配方或是在此期间更换配方就停用
            if (getMatchingRecipes(player).isEmpty()) {
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
                        DropUtil.summonDrop(results, level, vec3);
                    }
                }

                //减少耐久度
                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(e.getUsedItemHand()));
                setRecipeTime(stack, 0);
            }
        }
        return stack;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public static final HumanoidModel.ArmPose ROLLING = HumanoidModel.ArmPose.create("ROLLING", true, (model, entity, arm) -> {
                int time = 20 - Math.abs(entity.getUseItemRemainingTicks() % 40 - 20);
                float timeInRadians = (float) Math.toRadians(time);
                model.leftArm.yRot = (float) (-Math.PI / 10F + timeInRadians);
                model.leftArm.xRot = (float) (-Math.PI * 100 / 180);
                model.leftArm.zRot = (float) (Math.PI / 2);
                model.rightArm.yRot = (float) (-Math.PI / 10F + timeInRadians);
                model.rightArm.xRot = (float) (-Math.PI * 80 / 180);
                model.rightArm.zRot = (float) (Math.PI / 2);
            });
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack itemStack) {
                if (entity.getUseItem().is(itemStack.getItem())) {
                    return ROLLING;
                }
                return IClientItemExtensions.super.getArmPose(entity, hand, itemStack);
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if (player.getUseItem().is(itemInHand.getItem())) {
                    int time = 20 - Math.abs(player.getUseItemRemainingTicks() % 40 - 20);
                    poseStack.translate(0.1, 0.2, -time / 20f);
                    poseStack.mulPose(Axis.ZN.rotationDegrees(102.5f));
                    poseStack.mulPose(Axis.XP.rotationDegrees(25));
                }
                return IClientItemExtensions.super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
            }
        });
    }

    /**
     * 主动松开右键也能结束使用
     */
    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int p_41415_) {
        entity.stopUsingItem();
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
                    DropUtil.summonDrop(Block.getDrops(state, sl, pos, null), level, pos, move);
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
                                    DropUtil.summonDrop(Block.getDrops(tState, sl, pPos, null), level, pPos, move);
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

