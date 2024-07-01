package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.steaming.ISteamingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SteamerItem extends BlockItem {

    public SteamerItem() {
        super(IMBlocks.STEAMER.get(), new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new SteamerItemRenderer();
            }
        });
    }

    public static List<ItemStackHandler> getInvFromItem(ItemStack steamerItemStack) {
        List<ItemStackHandler> invList = new ArrayList<>();
        CompoundTag beT = BlockItem.getBlockEntityData(steamerItemStack);
        if (beT != null && beT.contains(ISteamingRecipe.STEAMER_INV_LIST)) {
            ListTag listTag = beT.getList(ISteamingRecipe.STEAMER_INV_LIST, ListTag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                ItemStackHandler s = new ItemStackHandler();
                s.deserializeNBT(listTag.getCompound(i));
                invList.add(s);
            }
        }
        return invList;
    }

    public static int getStackAmount(ItemStack steamerItemStack) {
        CompoundTag beT = BlockItem.getBlockEntityData(steamerItemStack);
        if (beT != null && beT.contains(SteamerBlockEntity.STACK)) {
            return beT.getInt(SteamerBlockEntity.STACK);
        }
        return 0;
    }

    public static ItemStack getLid(ItemStack steamerItemStack) {
        if (steamerItemStack.isEmpty()) return ItemStack.EMPTY;
        CompoundTag beT = BlockItem.getBlockEntityData(steamerItemStack);
        if (beT != null && beT.contains(SteamerBlockEntity.LID)) {
            return ItemStack.of(beT.getCompound(SteamerBlockEntity.LID));
        }
        return ItemStack.EMPTY;
    }

}
