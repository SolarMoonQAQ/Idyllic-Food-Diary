package cn.solarmoon.idyllic_food_diary.common.block.base;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.util.CountingDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static cn.solarmoon.idyllic_food_diary.client.particle.vanilla.Eat.eat;

public abstract class AbstractConsumeFoodBlock extends BaseRemainBlock {

    private final int nutrition;
    private final float saturation;
    private final int eatCount;

    /**
     * @param eatCount 每消耗一次需要右键多少下
     */
    public AbstractConsumeFoodBlock(int eatCount, int nutrition, float saturation, Properties properties) {
        super(properties);
        this.eatCount = eatCount;
        this.nutrition = nutrition;
        this.saturation = saturation;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (getBlockLeft().asItem().getDefaultInstance().is(IMItemTags.FOOD_CONTAINER) && getThis(player, level, pos, state, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        if (player.canEat(false)) {

            int remain = state.getValue(REMAIN);
            if (remain > 0) {
                //计数装置
                CountingDevice counting = new CountingDevice(player, pos);
                IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount());
                //吃的声音
                level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                //吃的粒子效果
                if(level.isClientSide) eat(pos);
                BlockState targetState = state.setValue(REMAIN, remain - 1);
                if (counting.getCount() >= this.eatCount) {
                    level.setBlock(pos, targetState, 3);
                    player.getFoodData().eat(nutrition, saturation);
                    if (getEffect() != null) player.addEffect(getEffect());
                    counting.resetCount();
                    if (targetState.getValue(REMAIN) == 0) {
                        BlockState stateTo = getBlockLeft().defaultBlockState();
                        BlockUtil.replaceBlockWithAllState(state, stateTo, level, pos);
                    }
                }
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.PASS;
    }

    /**
     * 决定吃的声音类型
     */
    public SoundEvent getEatSound() {
        return SoundEvents.GENERIC_EAT;
    }

    /**
     * @return 决定吃完后留下的方块
     */
    public abstract Block getBlockLeft();

    @Nullable
    public MobEffectInstance getEffect() {
        return null;
    }

}
