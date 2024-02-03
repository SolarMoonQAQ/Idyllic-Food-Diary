package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.api.common.capability.IPlayerData;
import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.common.item.IOptionalRecipeItem;
import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.client.event.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.common.registry.IMCapabilities;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import cn.solarmoon.immersive_delight.util.AnimController;
import cn.solarmoon.immersive_delight.util.RollingPinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.Rolling.rolling;


public class RollingPinItem extends SwordItem implements IOptionalRecipeItem<RollingPinRecipe> {

    private boolean recipeMatches;
    private RollingPinRecipe selectedRecipe;
    private final List<RollingPinRecipe> matchingRecipes;
    /**
     * 涵盖了所有匹配配方的可输出物品的集合
     */
    private final List<ItemStack> optionalOutputs;
    private int time;
    private Block equalBlock;
    private BlockPos equalBlockPos;

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
        return time;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.NONE;
    }

    /**
     * 擀面杖只能用于擀面配方中已有的方块
     * 这一步用于决定擀面杖是否开始使用并激活动作
     * 并从配方中读取相应的time值
     */
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        equalBlock = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        equalBlockPos = context.getClickedPos();
        //限制擀面空间
        BlockPos exceptPos;
        exceptPos = new BlockPos(Objects.requireNonNull(context.getPlayer()).getOnPos().getX() + context.getPlayer().getDirection().getStepX(), context.getPlayer().getOnPos().getY() + 1, context.getPlayer().getOnPos().getZ() + context.getPlayer().getDirection().getStepZ());
        if (equalBlockPos.equals(exceptPos) || equalBlockPos.equals(exceptPos.above())) {
            //先更新配方，再检查当前使用的方块是否是一个配方的输入物
            if(context.getLevel().isClientSide) {
                RollingPinHelper.updatePossibleOutputs(equalBlock);
            }

            ItemStack heldStack = context.getItemInHand();
            Player player = context.getPlayer();
            InteractionHand hand = context.getHand();
            Level level = context.getLevel();

            if (heldStack.getItem() instanceof RollingPinItem pin) {
                if (recipeMatches()) {
                    pin.time = getSelectedRecipe(player).getTime();
                    player.startUsingItem(hand);
                    new AnimController(player).playAnim(20, "waving");
                } else {
                    pin.time = 0;
                }
            }

        }
        return InteractionResult.PASS;
    }

    /**
     * 这一步用于在使用期间，如果发生视角转移（也就是目视方块配方不匹配擀面配方了）等情况，就停止使用
     * 同时在使用期间，每3tick就播放一次擀面声音（擀面+被擀方块的混合声）
     */
    private int tickCounter = 0;
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int i) {
        if (stack.getItem() instanceof RollingPinItem pin) {
            if (!pin.recipeMatches()) {
                new AnimController(entity).stopAnim(10);
                entity.stopUsingItem();
            }
            if (level.isClientSide) {
                rolling(equalBlockPos);
            } else pin.tickCounter++;
            if (pin.tickCounter >= 3) {
                level.playSound(null, equalBlockPos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, equalBlockPos, equalBlock.getSoundType(equalBlock.defaultBlockState(), level, equalBlockPos, null).getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
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
        if (stack.getItem() instanceof RollingPinItem pin) {
            if (getUseDuration(entity.getUseItem()) - entity.getUseItemRemainingTicks() >= pin.time && pin.time > 0) {

                RollingPinRecipe recipe = pin.getSelectedRecipe(entity);
                if (recipe.getOutput() != null) {

                }

                ItemStack output;
                Block selectedBlock = Blocks.AIR;
                //输出方块
                if (level.isClientSide) {
                    //保底更新
                    RollingPinHelper.updatePossibleOutputs(equalBlock);
                    if (RollingPinClientEvent.possibleOutputs != null && !RollingPinClientEvent.possibleOutputs.isEmpty()) {
                        output = RollingPinClientEvent.possibleOutputs.get(RollingPinClientEvent.currentRecipeIndex);
                        selectedBlock = Block.byItem(output.getItem());
                        ServerPackSerializer.sendPacket(equalBlockPos, selectedBlock, stack, "rollingOutput");
                    }
                }

                //输出RESULTS的随机的输出(掉落物高度根据碰撞箱决定)
                if (level.isClientSide) {
                    if (!RollingPinClientEvent.actualResults.isEmpty()) {
                        for (ItemStack item : RollingPinClientEvent.actualResults) {
                            ServerPackSerializer.sendPacket(equalBlockPos, selectedBlock, item, "rollingResults");
                        }
                    }
                }

                //减少耐久度
                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(e.getUsedItemHand()));

                entity.stopUsingItem();
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
     * 设置擀面杖的修复物为任意原木类
     */
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        Ingredient logs = Ingredient.of(ItemTags.LOGS);
        return logs.test(repair);
    }

    @Override
    public RecipeType<RollingPinRecipe> getRecipe() {
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
    public RollingPinRecipe getSelectedRecipe(Entity entity) {
        if (entity instanceof Player player) {
            IPlayerData playerData = CapabilityUtil.getData(player, IMCapabilities.PLAYER_DATA);
            RecipeSelectorData selector = playerData.getRecipeSelectorData();
            return getMatchingRecipes().get(selector.getRecipeIndex());
        }
        return null;
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

}

