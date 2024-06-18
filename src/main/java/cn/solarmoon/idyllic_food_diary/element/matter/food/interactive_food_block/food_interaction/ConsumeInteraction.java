package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockEntity;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.BaseInteractionBlock;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.capability.CountingDevice;
import cn.solarmoon.solarmoon_core.api.util.PlayerUtil;
import cn.solarmoon.solarmoon_core.feature.capability.IPlayerData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static cn.solarmoon.idyllic_food_diary.util.ParticleSpawner.eat;

public class ConsumeInteraction {

    private int preEatCount;
    private FoodProperties foodProperty;
    private SoundEvent eatSound;

    public ConsumeInteraction(int preEatCount, FoodProperties foodProperty) {
        this.preEatCount = preEatCount;
        this.foodProperty = foodProperty;
        this.eatSound = SoundEvents.GENERIC_EAT;
    }

    public ConsumeInteraction(int preEatCount, FoodProperties foodProperty, SoundEvent eatSound) {
        this.preEatCount = preEatCount;
        this.foodProperty = foodProperty;
        this.eatSound = eatSound;
    }

    public ConsumeInteraction setPreEatCount(int preEatCount) {
        this.preEatCount = preEatCount;
        return this;
    }

    public ConsumeInteraction setFoodProperty(FoodProperties foodProperty) {
        this.foodProperty = foodProperty;
        return this;
    }

    public ConsumeInteraction setEatSound(SoundEvent eatSound) {
        this.eatSound = eatSound;
        return this;
    }

    public int getPreEatCount() {
        return preEatCount;
    }

    public FoodProperties getFoodProperty() {
        return foodProperty;
    }

    public SoundEvent getEatSound() {
        return eatSound;
    }

    /**
     * 进行进食的交互
     * @return 交互成功返回true
     */
    public boolean doConsume(ItemStack heldItem, Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        if (fb == null) return false;
        if (player.canEat(false)) {
            int remain = state.getValue(BaseInteractionBlock.INTERACTION);
            if (remain > 0) {
                //计数装置
                IPlayerData p = player.getCapability(SolarCapabilities.PLAYER_DATA).orElse(null);
                if (p == null) return false;
                CountingDevice counting = p.getCountingDevice();
                counting.setCount(counting.getCount() + 1, pos);
                IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount(), player);
                //吃的声音
                level.playSound(null, pos, eatSound, SoundSource.PLAYERS, 1.0F, 1.0F);
                //吃的粒子效果
                eat(pos, player, level);
                BlockState targetState = state.setValue(BaseInteractionBlock.INTERACTION, remain - 1);
                if (counting.getCount() >= preEatCount) {
                    level.setBlock(pos, targetState, 3);
                    PlayerUtil.eat(player, foodProperty);
                    counting.resetCount();
                    if (targetState.getValue(BaseInteractionBlock.INTERACTION) == 0) {
                        BlockUtil.replaceBlockWithAllState(state, fb.getContainerLeft(), level, pos);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public ConsumeInteraction copy() {
        return new ConsumeInteraction(
                this.preEatCount,
                this.foodProperty,
                this.eatSound
        );
    }

}
