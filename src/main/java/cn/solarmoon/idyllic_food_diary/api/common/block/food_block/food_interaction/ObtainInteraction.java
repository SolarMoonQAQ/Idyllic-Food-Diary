package cn.solarmoon.idyllic_food_diary.api.common.block.food_block.food_interaction;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block.BaseInteractionBlock;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.device.ItemMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ObtainInteraction {

    private ItemStack drop;
    private ItemMatcher matcher;
    private DropForm dropForm;
    private ObtainingMethod method;
    private Block blockLeft;
    private SoundEvent sound;

    public ObtainInteraction(ItemStack drop, ItemMatcher matcher, DropForm dropForm, ObtainingMethod method) {
        this.drop = drop;
        this.matcher = matcher;
        this.dropForm = dropForm;
        this.method = method;
        this.blockLeft = Blocks.AIR;
        this.sound = SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    public ObtainInteraction(ItemStack drop, ItemMatcher matcher, DropForm dropForm, ObtainingMethod method, Block blockLeft) {
        this.drop = drop;
        this.matcher = matcher;
        this.dropForm = dropForm;
        this.method = method;
        this.blockLeft = blockLeft;
        this.sound = SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    public ObtainInteraction(ItemStack drop, ItemMatcher matcher, DropForm dropForm, ObtainingMethod method, Block blockLeft, SoundEvent sound) {
        this.drop = drop;
        this.matcher = matcher;
        this.dropForm = dropForm;
        this.method = method;
        this.blockLeft = blockLeft;
        this.sound = sound;
    }

    public ObtainInteraction setDrop(ItemStack drop) {
        this.drop = drop;
        return this;
    }

    public ObtainInteraction setMatcher(ItemMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public ObtainInteraction setDropForm(DropForm dropForm) {
        this.dropForm = dropForm;
        return this;
    }

    public ObtainInteraction setMethod(ObtainingMethod method) {
        this.method = method;
        return this;
    }

    public ObtainInteraction setBlockLeft(Block blockLeft) {
        this.blockLeft = blockLeft;
        return this;
    }

    public ObtainInteraction setSound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public ItemMatcher getMatcher() {
        return matcher;
    }

    public DropForm getDropForm() {
        return dropForm;
    }

    public ObtainingMethod getMethod() {
        return method;
    }

    public Block getBlockLeft() {
        return blockLeft;
    }

    public SoundEvent getSound() {
        return sound;
    }

    /**
     * 进行食物获取的交互
     * @return 交互成功返回true
     */
    public boolean doObtain(ItemStack heldItem, Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        if (matcher.isItemEqual(heldItem)) {
            // 首先替换方块到下一阶段，如果目标阶段为0则放置留存方块
            int remain = state.getValue(BaseInteractionBlock.INTERACTION);
            BlockState targetState = state.setValue(BaseInteractionBlock.INTERACTION, remain - 1);
            level.setBlock(pos, targetState, 3);
            if (targetState.getValue(BaseInteractionBlock.INTERACTION) == 0) {
                BlockState stateTo = blockLeft.defaultBlockState();
                BlockUtil.replaceBlockWithAllState(state, stateTo, level, pos);
            }
            // 根据method决定物品是消耗还是消耗耐久
            // 先消耗物品防止挤占槽位
            if (!matcher.isEmpty()) {
                if (method == ObtainingMethod.SERVE) {
                    heldItem.shrink(1);
                } else if (method == ObtainingMethod.SPLIT) {
                    heldItem.hurtAndBreak(1, player, pl -> pl.swing(hand));
                }
            }
            // 根据dropForm决定生成掉落物还是直接进入玩家背包
            if (dropForm == DropForm.DROP) {
                LevelSummonUtil.summonDrop(drop, level, pos);
            } else if (dropForm == DropForm.INVENTORY) {
                LevelSummonUtil.addItemToInventory(player, drop);
            }
            level.playSound(null, pos, sound, SoundSource.PLAYERS);
            return true;
        } else {
            // 基本的条件不满足就发送消息提示
            Component need = matcher.getHoverName();
            player.displayClientMessage(method.getMessage(need), true);
            return false;
        }
    }

    public ObtainInteraction copy() {
        return new ObtainInteraction(
                this.drop.copy(),
                this.matcher,
                this.dropForm,
                this.method,
                this.blockLeft,
                this.sound
        );
    }


    public enum ObtainingMethod {
        SERVE, SPLIT;

        public Component getMessage(Object... objects) {
            if (this == SERVE) {
                return IdyllicFoodDiary.TRANSLATOR.set("message", "obtainable_food.container_required", objects);
            } else if (this == SPLIT) {
                return IdyllicFoodDiary.TRANSLATOR.set("message", "obtainable_food.tool_required", objects);
            }
            return Component.empty();
        }
    }

    /**
     * 标识获取remain对应物品的方式
     */
    public enum DropForm {
        DROP, INVENTORY
    }

}