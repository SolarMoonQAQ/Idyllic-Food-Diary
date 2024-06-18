package cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.util.ParticleSpawner;
import cn.solarmoon.solarmoon_core.api.capability.CountingDevice;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.feature.capability.IPlayerData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
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

public class ServicePlateBlock extends BaseCookwareBlock {

    private final int eatCount = 5;

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
        if (plate.putItem(player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        if (player.isCrouching() && plate.takeItem(player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        ItemStack food = plate.getLastItem();
        if (player.canEat(false) && food.isEdible()) {
            //计数装置
            IPlayerData p = player.getCapability(SolarCapabilities.PLAYER_DATA).orElse(null);
            if (p == null) return InteractionResult.FAIL;
            CountingDevice counting = p.getCountingDevice();
            counting.setCount(counting.getCount() + 1, pos);
            IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount(), player);
            //吃的声音
            level.playSound(null, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            //吃的粒子效果
            ParticleSpawner.eat(food, player, level);
            if (counting.getCount() >= this.eatCount) {
                //吃掉！
                ItemStack give = food.finishUsingItem(level, player);
                if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, give);
                else food.shrink(1); //创造模式也消耗食物
                //重置计数
                counting.resetCount();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public SoundEvent getEatSound() {
        return SoundEvents.GENERIC_EAT;
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
