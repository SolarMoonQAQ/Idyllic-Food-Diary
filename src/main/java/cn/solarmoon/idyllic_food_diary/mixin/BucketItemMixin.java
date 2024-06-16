package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.Temp;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item {

    public BucketItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public Component getName(ItemStack bucket) {
        Component origin = super.getName(bucket);
        var op = FluidUtil.getFluidContained(bucket);
        if (op.isPresent()) {
            return Temp.getOrCreateFluidTemp(op.get()).getLevel().getPrefix();
        }
        return origin;
    }

}
