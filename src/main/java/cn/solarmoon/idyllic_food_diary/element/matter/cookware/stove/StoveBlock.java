package cn.solarmoon.idyllic_food_diary.element.matter.cookware.stove;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlockEntity;
import cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature.IInlineBlockMethodCall;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.common.block.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.common.block.ILitBlock;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collections;
import java.util.List;

public class StoveBlock extends BaseCookwareBlock implements ILitBlock, IBlockUseCaller {

    public StoveBlock() {
        super(BlockBehaviour.Properties.of()
                .lightLevel((state) -> state.getValue(LIT) ? 13 : 0)
                .sound(SoundType.STONE)
                .strength(1.5f));
    }

    public StoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        StoveBlockEntity stove = (StoveBlockEntity) level.getBlockEntity(pos);
        if (stove == null) return InteractionResult.PASS;
        BlockEntity potB = stove.getPot();
        ItemStack heldItem = player.getItemInHand(hand);
        Vec3 click = hitResult.getLocation();
        double clickH = click.y - pos.getY();
        //-------当点击区域在上半部inside时放置锅，拆卸见attack---------------------------------------------------------------------------//
        if (clickH >= 10/16f && VecUtil.isInside(click, pos)) {
            if (stove.putItem(player, hand, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS);
                return InteractionResult.SUCCESS;
            }
        }
        //------当点击在上半区域内部且有锅时对锅进行操作-----------------------------------------------------------------------------------//
        if (Block.byItem(stove.getPotItem().getItem()) instanceof IInlineBlockMethodCall pot && VecUtil.isInside(click, pos,
                2/16f, 10/16f, 2/16f, 14/16f, 1, 14/16f, true)) {
            IdyllicFoodDiary.DEBUG.send("haha");
            return pot.doUse(level, pos, player, hand, potB);
        }
        //-----当点击在下半区域内部时放置柴火----------------------------------------------------------------//
        if (clickH <= 7/16f && VecUtil.isInside(click, pos) && stove.getInventory().isItemValid(1, heldItem) && stove.specialStorage(player, hand)) {
            level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }
        //----拿着打火石点击下半区域内部时点火---------------------------------------------------------------//
        if (!state.getValue(LIT) && clickH <= 7/16f && VecUtil.isInside(click, pos) && stove.hasFirewood()) {
            if (heldItem.getItem() instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(LIT, Boolean.TRUE), 11);
                heldItem.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
        }
        //----拿着水桶、铲子、水瓶灭火---------------------------------------------------------------//
        if (state.getValue(LIT) && clickH <= 7/16f && VecUtil.isInside(click, pos)) {
            if (heldItem.is(ItemTags.SHOVELS)) {
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 11);
                heldItem.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
            if (PotionUtils.getPotion(heldItem).equals(Potions.WATER)) {
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 11);
                heldItem.shrink(1);
                if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, new ItemStack(Items.GLASS_BOTTLE));
                return InteractionResult.SUCCESS;
            }
            if (heldItem.is(Items.WATER_BUCKET)) {
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 11);
                heldItem.shrink(1);
                if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, new ItemStack(Items.BUCKET));
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos pos, Level level, Player player, ItemStack itemStack, BlockHitResult hitResult, InteractionHand interactionHand) {
        if (hitResult.getLocation().y - pos.getY() <= 0.5) {
            return Event.Result.ALLOW;
        } // 防止不能在上方放方块
        return Event.Result.DEFAULT;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (player.isCrouching() && player.getMainHandItem().isEmpty()) {
            StoveBlockEntity stove = (StoveBlockEntity) level.getBlockEntity(pos);
            if (stove != null && !stove.getPotItem().isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, stove.getPotItem().split(1));
                stove.setChanged();
                level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS);
            }
        }
        super.attack(state, level, pos, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        StoveBlockEntity stove = (StoveBlockEntity) blockEntity;
        BlockEntity pot = stove.getPot();
        stove.updatePotItem();
        stove.tryControlLit();
        if (pot instanceof CookingPotBlockEntity cPot) {
            if (!cPot.tryStirFrying()) {
                if (!cPot.tryStew()) {
                    if (!cPot.trySimmer()) {
                        if (!cPot.tryBoilFood()) {
                            if (!cPot.tryBoilWater()) {
                                cPot.tryDrainHotFluid();
                            }
                        }
                    }
                }
            } //按顺序执行配方

            if (pot.getLevel() == null) {
                pot.setLevel(level);
            }
        }
        if (stove.getPotItem().isEmpty() && pot != null) stove.resetPot(); // 防止嵌入be不消失
    }

    /**
     * 返璞归真，直接把东西全爆出来
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ResourceLocation resourcelocation = this.getLootTable();
        if (resourcelocation == BuiltInLootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootParams lootparams = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
            ServerLevel serverlevel = lootparams.getLevel();
            LootTable loottable = serverlevel.getServer().getLootData().getLootTable(resourcelocation);
            StoveBlockEntity stove = (StoveBlockEntity) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            var stacks = loottable.getRandomItems(lootparams);
            if (stove != null) {
                stacks.addAll(stove.getStacks());
            }
            return stacks;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape = Shapes.joinUnoptimized(Shapes.block(), Block.box(5, 2, 0, 11, 7, 12), BooleanOp.ONLY_FIRST);
        shape = Shapes.joinUnoptimized(shape, Block.box(5, 7, 4, 11, 10, 12), BooleanOp.ONLY_FIRST);
        shape = Shapes.joinUnoptimized(shape, Block.box(2, 10, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
        return VoxelShapeUtil.rotateShape(state.getValue(FACING), shape);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.STOVE.get();
    }

}
