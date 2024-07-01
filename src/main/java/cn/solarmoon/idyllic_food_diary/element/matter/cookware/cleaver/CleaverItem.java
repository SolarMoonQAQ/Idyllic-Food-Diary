package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cleaver;

import cn.solarmoon.idyllic_food_diary.data.IMBlockTags;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardBlock;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.chopping.ChoppingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 菜刀
 * 属于剑和斧的结合物
 * 具有配方，右键可以将物品切成结果
 * 具体实现在event中
 */
public class CleaverItem extends DiggerItem implements IOptionalRecipeItem<ChoppingRecipe> {

    public CleaverItem() {
        super(3, -2.4f, Tiers.IRON,
                IMBlockTags.MINEABLE_WITH_CLEAVER,
                new Item.Properties().durability(2048));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack useItem = context.getItemInHand();
        Player player = context.getPlayer();
        if (player == null) return super.useOn(context);
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        //这里只能是服务端侧
        return getSelectedRecipe(useItem, player).map(recipe -> {
            //输出（生成掉落物）
            List<ItemStack> results = recipe.getRolledResults(player);
            for (var stack : results) {
                DropUtil.summonDrop(stack, level, pos.getCenter());
            }
            if (state.getBlock() instanceof CuttingBoardBlock && blockEntity != null) {
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(inv -> {
                    ItemStack input = getAllItemFromInv(inv).get(0);
                    spawnCuttingParticles(level, pos, input, 5);
                    input.shrink(1);
                    blockEntity.setChanged();
                });
            } else level.destroyBlock(pos, false);
            useItem.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));
            level.playSound(null, pos, SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS, 1F, 1.5F);
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 10F, 1F);
            return InteractionResult.SUCCESS;
        }).orElse(InteractionResult.FAIL);
    }

    public static void spawnCuttingParticles(Level level, BlockPos pos, ItemStack stack, int count) {
        Block byStack = Block.byItem(stack.getItem());
        ParticleOptions particle = byStack == Blocks.AIR ? new ItemParticleOption(ParticleTypes.ITEM, stack) : new BlockParticleOption(ParticleTypes.BLOCK, byStack.defaultBlockState());
        for (int i = 0; i < count; ++i) {
            Vec3 vec3d = new Vec3(((double) level.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) level.random.nextFloat() - 0.5D) * 0.1D);
            level.addParticle(particle, pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
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
    public RecipeType<ChoppingRecipe> getRecipeType() {
        return IMRecipes.CHOPPING.get();
    }

    @Override
    public boolean recipeMatches(ChoppingRecipe recipe, BlockState state, Level level, BlockHitResult hitResult, Player player) {
        boolean isOnBoard = state.is(IMBlockTags.CUTTING_BOARD);
        if (isOnBoard) { // 在砧板上时，当且仅当只存在一个匹配的物品时才能切
            BlockEntity blockEntity = level.getBlockEntity(hitResult.getBlockPos());
            if (blockEntity == null) return false;
            return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(inv -> {
                List<ItemStack> allIn = getAllItemFromInv(inv);
                if (allIn.size() != 1) return false;
                return recipe.input().test(allIn.get(0));
            }).orElse(false);
        }
        else {
            ItemStack hitStack = state.getCloneItemStack(hitResult, level, hitResult.getBlockPos(), player);
            return recipe.input().test(hitStack);
        }
    }

    public static List<ItemStack> getAllItemFromInv(IItemHandler inv) {
        List<ItemStack> allIn = new ArrayList<>();
        IntStream.range(0, inv.getSlots()).forEach(i -> {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) allIn.add(stack);
        });
        return allIn;
    }

    @Override
    public List<ItemStack> getItemsOnGui(Player player) {
        List<ItemStack> optionalOutputs = new ArrayList<>();
        for (ChoppingRecipe recipe : getMatchingRecipes(player)) {
            ItemStack put = recipe.getResults().get(0);
            optionalOutputs.add(put);
        }
        return optionalOutputs;
    }

}
