package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * 菜刀
 * 属于剑和斧的结合物
 * 具有配方，右键可以将物品切成结果
 * 具体实现在event中
 */
public class ChineseCleaverItem extends DiggerItem {

    public ChineseCleaverItem() {
        super(4, -2.4f, Tiers.IRON, IMBlockTags.MINEABLE_WITH_CLEAVER, new Item.Properties().durability(2048));
    }

    /**
     * 挖掘速度增加剑类型
     */
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        } else if (state.is(BlockTags.SWORD_EFFICIENT)) {
            return 1.5F;
        }
        return super.getDestroySpeed(stack, state);
    }

    /**
     * 可挖蜘蛛网
     */
    public boolean isCorrectToolForDrops(BlockState state) {
        return super.isCorrectToolForDrops(state) || state.is(Blocks.COBWEB);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    /**
     * 设置修复物为铁
     */
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return repair.is(Items.IRON_INGOT);
    }

}
