package cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.util.ParticleSpawner;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.capability.CountingDevice;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;

public class ServicePlateBlock extends SyncedEntityBlock implements IHorizontalFacingBlock {

    public ServicePlateBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.GLASS)
                .strength(0.7F)
                .noOcclusion()
        );
    }

    public ServicePlateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        ServicePlateBlockEntity plate = (ServicePlateBlockEntity) level.getBlockEntity(pos);
        if (plate == null) return InteractionResult.PASS;
        ItemStackHandler inv = plate.getInventory();
        if (ItemHandlerUtil.putItem(inv, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        if (player.isCrouching() && ItemHandlerUtil.takeItem(inv, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        ItemStack food = plate.getLastItem();
        if (player.canEat(false) && food.isEdible()) {
            //计数装置
            player.getCapability(SolarCapabilities.PLAYER_DATA).ifPresent(p -> {
                CountingDevice counting = p.getCountingDevice();
                counting.setCount(counting.getCount() + 1, pos);
                //吃的声音
                level.playSound(null, pos, food.getEatingSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                //吃的粒子效果
                ParticleSpawner.eat(food, player, level);
                if (counting.getCount() >= getEatCount()) {
                    //吃掉！
                    ItemStack give = food.finishUsingItem(level, player);
                    if (!player.isCreative()) {
                        if (!give.isEmpty()) DropUtil.summonDrop(give, level, pos.getCenter());
                        // 保证能够推出食用后的物品（比如碗装食物吃掉后能够推出碗）
                    }
                    else {
                        food.shrink(1); //创造模式也消耗食物
                    }
                    //重置计数
                    counting.resetCount();
                }
            });
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public int getEatCount() {
        return 5;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        getThis(player, level, pos, state, InteractionHand.MAIN_HAND, true);
        super.attack(state, level, pos, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        // AbstractServicePlateBlockEntity plate = (AbstractServicePlateBlockEntity) getter.getBlockEntity(pos);
        // if (plate == null) return Shapes.block();
        // int add = plate.getStacks().size(); <- 想让堆叠的食物也具有碰撞箱，但是堆叠太高了先不改
        return Block.box(1, 0, 1, 15, 2, 15);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.PLATE.get();
    }

}
