package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseContainerBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTCBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.network.IClientPackHandler;
import cn.solarmoon.immersive_delight.api.registry.Capabilities;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.util.namespace.NETList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;


public class ClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, CompoundTag tag, float f, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case NETList.SYNC_FURNACE -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int i = 0; i < stacks.size(); i++) {
                        e.setItem(i, stacks.get(i));
                    }
                }
            }
            case NETList.SYNC_UP_STEP -> {
                player.setMaxUpStep(f);
            }
            case NETList.SYNC_INDEX -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setIndex((int) f, recipeType);
            }
            case NETList.SYNC_RECIPE_INDEX -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setRecipeIndex((int) f, recipeType);
            }
        }
    }

}
