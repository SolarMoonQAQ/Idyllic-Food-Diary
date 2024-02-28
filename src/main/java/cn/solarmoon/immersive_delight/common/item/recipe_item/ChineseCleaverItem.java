package cn.solarmoon.immersive_delight.common.item.recipe_item;

import cn.solarmoon.immersive_delight.common.recipe.CleaverRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import cn.solarmoon.solarmoon_core.common.item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜刀
 * 属于剑和斧的结合物
 * 具有配方，右键可以将物品切成结果
 * 具体实现在event中
 */
public class ChineseCleaverItem extends DiggerItem implements IOptionalRecipeItem<CleaverRecipe> {

    private boolean recipeMatches;
    private final List<CleaverRecipe> matchingRecipes;
    /**
     * 涵盖了所有匹配配方的单个可输出物品的集合
     */
    private final List<ItemStack> optionalOutputs;

    public ChineseCleaverItem() {
        super(3, -2.4f, Tiers.IRON,
                IMBlockTags.MINEABLE_WITH_CLEAVER,
                new Item.Properties().durability(2048));
        this.matchingRecipes = new ArrayList<>();
        this.optionalOutputs = new ArrayList<>();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack useItem = context.getItemInHand();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        //这里只能是服务端侧
        if (useItem.getItem() instanceof IOptionalRecipeItem<?> op && !level.isClientSide) {
            CleaverRecipe recipe = (CleaverRecipe) op.getSelectedRecipe(player);
            if (op.recipeMatches()) {
                //输出（生成掉落物）
                List<ItemStack> results = recipe.rollResults(player);
                for (var stack : results) {
                    LevelSummonUtil.summonDrop(stack, level, pos);
                }
                if (state.is(IMBlockTags.CUTTING_BOARD) && blockEntity != null) {
                    ItemStackHandler inventory = ContainerUtil.getInventory(blockEntity);
                    inventory.extractItem(0, 1, false);
                    spawnCuttingParticles(level, pos, inventory.getStackInSlot(0), 5);
                    blockEntity.setChanged();
                } else level.destroyBlock(pos, false);
                useItem.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));
                level.playSound(null, pos, SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS, 1F, 1.5F);
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 10F, 1F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    public static void spawnCuttingParticles(Level level, BlockPos pos, ItemStack stack, int count) {
        for (int i = 0; i < count; ++i) {
            Vec3 vec3d = new Vec3(((double) level.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) level.random.nextFloat() - 0.5D) * 0.1D);
            if (level instanceof ServerLevel) {
                ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
            } else {
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
            }
        }
    }

    /**
     * 挖掘速度增加剑类型
     */
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        } else if (state.is(BlockTags.SWORD_EFFICIENT)) {
            return 1.5F;
        }
        return super.getDestroySpeed(stack, state);
    }

    /**
     * 可挖蜘蛛网、小大花类
     */
    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return super.isCorrectToolForDrops(state)
                || state.is(Blocks.COBWEB)
                || state.is(BlockTags.SMALL_FLOWERS)
                || state.is(BlockTags.TALL_FLOWERS);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    /**
     * 设置修复物为铁
     */
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return repair.is(Items.IRON_INGOT);
    }

    @Override
    public RecipeType<CleaverRecipe> getRecipeType() {
        return IMRecipes.CLEAVER.get();
    }

    @Override
    public boolean recipeCheckAndUpdate(CleaverRecipe recipe, ItemStack hitStack, Level level, BlockHitResult hitResult, Player player) {
        BlockState hitState = Block.byItem(hitStack.getItem()).defaultBlockState();
        boolean flag = hitState.is(IMBlockTags.CUTTING_BOARD);
        BlockEntity blockEntity = level.getBlockEntity(hitResult.getBlockPos());
        return recipe.getInput().test(hitStack) || (flag && blockEntity != null && recipe.getInput().test(ContainerUtil.getInventory(blockEntity).getStackInSlot(0)));
    }

    @Override
    public boolean recipeMatches() {
        return recipeMatches;
    }

    @Override
    public void setRecipeMatch(boolean b) {
        recipeMatches = b;
    }

    @Override
    public List<CleaverRecipe> getMatchingRecipes() {
        return matchingRecipes;
    }

    @Override
    public List<ItemStack> getOptionalOutputs() {
        //先重置，否则会一直累加
        optionalOutputs.clear();
        for (CleaverRecipe recipe : getMatchingRecipes()) {
            ItemStack put = recipe.getResults().get(0);
            optionalOutputs.add(put);
        }
        return optionalOutputs;
    }

}
