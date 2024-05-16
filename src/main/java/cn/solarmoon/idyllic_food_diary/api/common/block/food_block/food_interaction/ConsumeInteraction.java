package cn.solarmoon.idyllic_food_diary.api.common.block.food_block.food_interaction;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block.BaseInteractionBlock;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.player.CountingDevice;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.util.PlayerUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static cn.solarmoon.idyllic_food_diary.api.util.ParticleSpawner.eat;

public class ConsumeInteraction {

    private int preEatCount;
    private FoodProperties foodProperty;
    private Block blockLeft;
    private SoundEvent eatSound;

    public ConsumeInteraction(int preEatCount, FoodProperties foodProperty) {
        this.preEatCount = preEatCount;
        this.foodProperty = foodProperty;
        this.blockLeft = Blocks.AIR;
        this.eatSound = SoundEvents.GENERIC_EAT;
    }

    public ConsumeInteraction(int preEatCount, FoodProperties foodProperty, Block blockLeft) {
        this.preEatCount = preEatCount;
        this.foodProperty = foodProperty;
        this.blockLeft = blockLeft;
        this.eatSound = SoundEvents.GENERIC_EAT;
    }

    public ConsumeInteraction(int preEatCount, FoodProperties foodProperty, Block blockLeft, SoundEvent eatSound) {
        this.preEatCount = preEatCount;
        this.foodProperty = foodProperty;
        this.blockLeft = blockLeft;
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

    public ConsumeInteraction setBlockLeft(Block blockLeft) {
        this.blockLeft = blockLeft;
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

    public Block getBlockLeft() {
        return blockLeft;
    }

    public SoundEvent getEatSound() {
        return eatSound;
    }

    /**
     * 进行进食的交互
     * @return 交互成功返回true
     */
    public boolean doConsume(ItemStack heldItem, Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        if (player.canEat(false)) {
            int remain = state.getValue(BaseInteractionBlock.INTERACTION);
            if (remain > 0) {
                //计数装置
                CountingDevice counting = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getCountingDevice();
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
                        BlockState stateTo = blockLeft.defaultBlockState();
                        BlockUtil.replaceBlockWithAllState(state, stateTo, level, pos);
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
                this.blockLeft,
                this.eatSound
        );
    }

}
