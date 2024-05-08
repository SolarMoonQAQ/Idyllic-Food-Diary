package cn.solarmoon.idyllic_food_diary.api.common.block.entity_block;

import cn.solarmoon.idyllic_food_diary.api.util.ParticleSpawner;
import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.ServicePlateBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.player.CountingDevice;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static cn.solarmoon.idyllic_food_diary.api.util.ParticleSpawner.eat;

public abstract class AbstractServicePlateEntityBlock extends BasicEntityBlock {

    private final int eatCount = 4;

    public AbstractServicePlateEntityBlock(Properties properties) {
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
            CountingDevice counting = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getCountingDevice();
            counting.setCount(counting.getCount() + 1, pos);
            IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount());
            //吃的声音
            level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            //吃的粒子效果
            ParticleSpawner.eat(food, player);
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
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.PLATE.get();
    }

}
