package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.common.block_entity.GrillBlockEntity;
import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractGrillBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.immersive_delight.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.common.block.ILitBlock;
import cn.solarmoon.solarmoon_core.util.VecUtil;
import cn.solarmoon.immersive_delight.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseContainerEntityBlock;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public abstract class AbstractGrillEntityBlock extends BaseContainerEntityBlock implements ILitBlock {

    public AbstractGrillEntityBlock(Properties properties) {
        super(properties.lightLevel((state) -> state.getValue(LIT) ? 13 : 0));//点燃才亮
    }

    /**
     * 只能用手存入和篝火配方匹配的输入物<br/>
     * 存储逻辑为指哪存哪，有煤炭存煤炭
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        AbstractGrillBlockEntity grill = (AbstractGrillBlockEntity) level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        if (grill == null) return InteractionResult.PASS;
        //------------------------------------打火石点燃----------------------------------------//
        //打火石等点燃
        if (!state.getValue(LIT)) {
            if (heldItem.getItem() instanceof FlintAndSteelItem) {
                if (grill.getInventory().getStackInSlot(6).is(ItemTags.COALS)) {
                    level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                    heldItem.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
                    return InteractionResult.SUCCESS;
                }
                //这里返回fail防止打火石意外使用
                return InteractionResult.FAIL;
            } else if (heldItem.getItem() instanceof FireChargeItem) {
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        //------------------------------------存入煤炭----------------------------------------//
        //存入煤炭
        if (heldItem.is(ItemTags.COALS)) {
            //直接存入64，不知道为什么蹲下右键不触发
            ItemStack stack = heldItem.copy(); //科比防止假存储
            ItemStack result = grill.getInventory().insertItem(6, stack, false);
            if (!result.equals(stack)) {
                if (!player.isCreative()) player.setItemInHand(hand, result);
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                grill.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        //------------------------------------指哪取哪----------------------------------------//
        //存入其余食物
        //根据方向变换选框位置
        for (int i = 1; i <= 6; i++) {
            Direction direction = state.getValue(BasicEntityBlock.FACING);
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
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        if (blockEntity instanceof AbstractGrillBlockEntity grill) {
            ItemStackHandler inv = grill.getInventory();
            for (int i = 0; i < inv.getSlots(); i++) {
                CampfireCookingRecipe recipe = grill.getCheckedRecipe(i);
                if (recipe != null) {
                    grill.getRecipeTimes()[i] = recipe.getCookingTime() / 3;
                    grill.getTimes()[i] = grill.getTimes()[i]+ 1;
                    if (grill.getTimes()[i] >= recipe.getCookingTime() / 3) {
                        ItemStack out = recipe.getResultItem(level.registryAccess());
                        inv.setStackInSlot(i, out);
                        //这里设置slot必须在服务端侧同步（不知道为什么）
                        CompoundTag nbt = new CompoundTag();
                        nbt.put(SolarNBTList.INVENTORY, inv.serializeNBT());
                        if (level.isClientSide) IMPacks.SERVER_PACK.getSender().send(NETList.SYNC_SLOT_SET, pos, nbt);
                        grill.getTimes()[i] = 0;
                        grill.setChanged();
                    }
                } else {
                    grill.getTimes()[i] = 0;
                    grill.getRecipeTimes()[i] = 0;
                }
            }
            //消耗煤炭，控制lit属性
            if (state.getValue(LIT)) {
                //有燃料就保存其燃烧时间，并且消耗一个
                if (!grill.noFuel() && grill.saveBurnTime == 0) {
                    grill.saveBurnTime = ForgeHooks.getBurnTime(grill.getInventory().getStackInSlot(6), null);
                    grill.getInventory().getStackInSlot(6).shrink(1);
                    grill.setChanged();
                }
                grill.setBurnTime(grill.getBurnTime() + 1);
                //燃烧时间超过燃料所能提供，就刷新
                if (grill.getBurnTime() >= grill.saveBurnTime) {
                    grill.setBurnTime(0);
                    grill.saveBurnTime = 0;
                }
                //无燃料且没有燃烧时间了就停止lit
                if (grill.noFuel() && grill.getBurnTime() == 0) {
                    level.setBlock(pos, state.setValue(LIT, false), 3);
                    grill.setChanged();
                }
            }
            //同步燃烧时间
            if (!level.isClientSide) {
                IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_BURN_TIME, pos, grill.getBurnTime());
                IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_BURN_TIME_SAVING, grill.saveBurnTime);
            }
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
                    Direction direction = state.getValue(BasicEntityBlock.FACING);
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

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.GRILL.get();
    }

}
