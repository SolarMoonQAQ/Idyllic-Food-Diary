package cn.solarmoon.idyllic_food_diary.api.common.block.food_block;

import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.FoodBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMItemTags;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;

/**
 * 强大的可交互式食物方块，交互方式见food_interaction<br/>
 * 目前有两种交互，拿取食物和直接长按进食，每个阶段可单独配置<br/>
 * 未来可能会加入数据包自定义的方式来创建自己的复杂交互食物方块（但是鸽）
 */
public abstract class AbstractInteractiveFoodBlock extends BaseInteractionBlock {

    private final HashMap<Integer, Either<ObtainInteraction, ConsumeInteraction>> stageInteractionMap;

    public AbstractInteractiveFoodBlock(Properties properties) {
        super(properties);
        stageInteractionMap = new HashMap<>();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        initStageInteraction();
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        if (fb == null) return InteractionResult.FAIL;

        // 此处一定是container为基底才能快速拿起
        if (!fb.getContainer().isEmpty() && getThis(player, level, pos, state, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        int remain = state.getValue(INTERACTION);
        Either<ObtainInteraction, ConsumeInteraction> eiIntact = getStageInteractionMap().get(remain);
        if (eiIntact.left().isPresent()) {
            if (eiIntact.left().get().doObtain(heldItem, level, state, pos, player, hand)) {
                return InteractionResult.SUCCESS;
            }
        } else if (eiIntact.right().isPresent()) {
            if (eiIntact.right().get().doConsume(heldItem, level, state, pos, player, hand)) {
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.CONSUME; // 防止右键此类方块时使用手中物品
    }

    public void initStageInteraction() {
        for (int i = 1; i <= getMaxInteraction(); i++) {
            Either<ObtainInteraction, ConsumeInteraction> interaction = getSpecialInteraction(i);
            getStageInteractionMap().put(i, interaction);
        }
    }

    /**
     * 设定各个阶段的交互逻辑
     * @param stageIndex 当前阶段（Remain）数
     */
    public abstract Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex);

    public HashMap<Integer, Either<ObtainInteraction, ConsumeInteraction>> getStageInteractionMap() {
        return stageInteractionMap;
    }

    /**
     * 只能生存在有足够碰撞箱的方块上
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

}
