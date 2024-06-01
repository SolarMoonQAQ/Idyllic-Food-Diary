package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.common.block.ILitBlock;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class GrillBlock extends BaseCookwareBlock implements ILitBlock, IBlockUseCaller {

    public GrillBlock() {
        super(Properties.copy(Blocks.LANTERN)
                .lightLevel((state) -> state.getValue(LIT) ? 13 : 0)
                .noOcclusion());
    }

    public GrillBlock(Properties properties) {
        super(properties);
    }

    /**
     * 只能用手存入和篝火配方匹配的输入物<br/>
     * 存储逻辑为指哪存哪，有煤炭存煤炭
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        GrillBlockEntity grill = (GrillBlockEntity) level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        if (grill == null) return InteractionResult.PASS;
        //------------------------------------打火石点燃----------------------------------------//
        //打火石等点燃
        if (!state.getValue(LIT) && grill.getInventory().getStackInSlot(6).is(ItemTags.COALS)) {
            if (heldItem.getItem() instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                heldItem.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
        }
        //------------------------------------存入煤炭----------------------------------------//
        //存入煤炭
        if (heldItem.is(ItemTags.COALS)) {
            if (player.isCrouching()) {
                grill.putItem(player, hand, heldItem.getCount());
            }
            grill.putItem(player, hand, 1);
            level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
            grill.setChanged();
            return InteractionResult.SUCCESS;
        }
        //------------------------------------指哪取哪----------------------------------------//
        //存入其余食物
        //根据方向变换选框位置
        for (int i = 1; i <= 6; i++) {
            Direction direction = state.getValue(FACING);
            int c = i > 3 ? -1 : 1; //转变竖直方向
            int index = i > 3 ? i - 3 : i; //i>3时触底反弹
            Vec3 center = pos.getCenter();
            int blockScale = 14; //将整个矩形范围往内缩放
            double scale = blockScale / 16d;
            Vec3 base1 = center.add(-0.5 * scale + 1/3f * scale * (index - 1), 0.4375, -0.5 * scale * c);
            Vec3 base2 = center.add(-0.5 * scale + 1/3f * scale * index, 0.4375, 0);
            Vec3 v1 = VecUtil.rotateVec(base1, center, direction);
            Vec3 v2 = VecUtil.rotateVec(base2, center, direction);
            if (VecUtil.inRange(hit.getLocation(), v1, v2)) {
                //放入对应选框手持的物品
                if (!heldItem.isEmpty()) {
                    List<CampfireCookingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CAMPFIRE_COOKING);
                    for (var recipe : recipes) {
                        Ingredient in = recipe.getIngredients().get(0);
                        if (in.test(heldItem)) {
                            ItemStack result = grill.getInventory().insertItem(i - 1, heldItem, false);
                            if (!result.equals(heldItem)) {
                                if (!player.isCreative()) player.setItemInHand(hand, result);
                                if (state.getValue(LIT)) {
                                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);
                                } else level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                                grill.setChanged();
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
                //空手取出对应选框的物品
                if (heldItem.isEmpty() && !grill.getStacks().isEmpty()) {
                    ItemStack result = grill.getInventory().extractItem(i - 1, 1, false);
                    if (!result.isEmpty()) {
                        if (!player.isCreative()) {
                            LevelSummonUtil.addItemToInventory(player, result);
                        }
                        level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                        grill.setChanged();
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        //------------------------------------取出煤炭----------------------------------------//
        //选框在别的区域就尝试取出煤炭
        if (heldItem.isEmpty()) {
            //蹲下取64个，站着取1个
            ItemStack result;
            if (player.isCrouching()) {
                result = grill.getInventory().extractItem(6, 64, false);
            } else result = grill.getInventory().extractItem(6, 1, false);
            if (!result.isEmpty()) {
                if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, result);
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                grill.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        //----------------------------------------------------------------------------//
        return InteractionResult.PASS;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos blockPos, Level level, Player player, ItemStack itemStack, BlockHitResult blockHitResult, InteractionHand interactionHand) {
        return Event.Result.ALLOW;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        if (blockEntity instanceof GrillBlockEntity grill) {
            grill.tryCook();
            grill.tryControlLit();
        }
    }

    /**
     * 烧烤粒子/燃烧声音
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        GrillBlockEntity grill = (GrillBlockEntity) level.getBlockEntity(pos);
        if (grill != null && state.getValue(LIT)) {
            for (int i = 0; i < grill.getInventory().getSlots(); i++) {
                if (grill.getTimes()[i] != 0) {
                    Direction direction = state.getValue(FACING);
                    int c = i+1 > 3 ? -1 : 1; //转变竖直方向
                    int index = i+1 > 3 ? i - 3 : i; //i>3时触底反弹
                    Vec3 center = pos.getCenter();
                    Vec3 base1 = center.add(-0.5 + 1/6f + 1/3f * index, 0.5, -0.25 * c);
                    Vec3 v1 = VecUtil.rotateVec(base1, center, direction);
                    level.addParticle(ParticleTypes.SMOKE, v1.x, v1.y, v1.z, 0, 0.03, 0);
                }
            }
            //燃烧声音
            level.playLocalSound(pos, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1f, 1f, false);
        }
    }

    protected static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),

            Block.box(0D, 0.0D, 0D, 2D, 12D, 2D),
            Block.box(14D, 0.0D, 0D, 16D, 12D, 2D),
            Block.box(0D, 0.0D, 14D, 2D, 12D, 16D),
            Block.box(14D, 0.0D, 14D, 16D, 12D, 16D),

            Block.box(2D, 9D, 1D, 14D, 10D, 2D),
            Block.box(1D, 9D, 2D, 2D, 10D, 14D),
            Block.box(2D, 9D, 14D, 14D, 10D, 15D),
            Block.box(14D, 9D, 2D, 15D, 10D, 14D),

            Block.box(0D, 12D, 0D, 16D, 15D, 16D),

            Block.box(0D, 15D, 0D, 1D, 16D, 16D),
            Block.box(0D, 15D, 0D, 16D, 16D, 1D),
            Block.box(15D, 15D, 0D, 16D, 16D, 16D),
            Block.box(0D, 15D, 15D, 16D, 16D, 16D)
    };
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.or(SHAPE[0], SHAPE);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.GRILL.get();
    }

}
