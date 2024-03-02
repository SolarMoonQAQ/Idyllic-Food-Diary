package cn.solarmoon.immersive_delight.common.block.base;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.util.BlockUtil;
import cn.solarmoon.solarmoon_core.util.CountingDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import static cn.solarmoon.immersive_delight.client.particle.vanilla.Eat.eat;

public abstract class AbstractConsumeFoodBlock extends BaseWaterBlock {

    public static final IntegerProperty REMAIN = IntegerProperty.create("remain", 0, 10);

    private final int nutrition;
    private final float saturation;

    private final int eatCount;

    /**
     * @param count 能吃的次数（分割形态数）
     * @param eatCount 每消耗一次需要右键多少下
     */
    public AbstractConsumeFoodBlock(int count, int eatCount, int nutrition, float saturation, Properties properties) {
        super(properties);
        this.eatCount = eatCount;
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.registerDefaultState(this.getStateDefinition().any().setValue(REMAIN, count));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (getBlockLeft().asItem().getDefaultInstance().is(IMItemTags.FOOD_CONTAINER) && getThis(player, level, pos, state, hand)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1);
            return InteractionResult.SUCCESS;
        }

        if (player.canEat(false)) {

            int remain = state.getValue(REMAIN);
            if (remain > 0) {
                //计数装置
                CountingDevice counting = new CountingDevice(player, pos);
                ImmersiveDelight.DEBUG.send("点击次数：" + counting.getCount(), level);
                //吃的声音
                level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                //吃的粒子效果
                if(level.isClientSide) eat(pos);
                BlockState targetState = state.setValue(REMAIN, remain - 1);
                if (counting.getCount() >= this.eatCount) {
                    level.setBlock(pos, targetState, 3);
                    player.getFoodData().eat(nutrition, saturation);
                    counting.resetCount();
                    if (targetState.getValue(REMAIN) == 0) {
                        BlockState stateTo = getBlockLeft().defaultBlockState();
                        if (getBlockLeft() instanceof IBedPartBlock) {
                            BlockUtil.setBedPartBlock(state, stateTo, level, pos);
                        } // 双方块特殊放置
                        else BlockUtil.setBlockWithDirection(state, stateTo, level, pos);
                    }
                }
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(REMAIN);
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
    public Block getBlockLeft() {
        return Blocks.AIR;
    }

}
