package cn.solarmoon.idyllic_food_diary.api.common.block;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IExpGiver;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IPendingResult;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.FoodBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IWaterLoggedBlock;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseCookwareBlock extends BasicEntityBlock implements IHorizontalFacingBlock {

    public BaseCookwareBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack origin = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return origin;
        CompoundTag tag = origin.getOrCreateTag();
        if (blockEntity instanceof IPendingResult pending) {
            tag.put(NBTList.RESULT, pending.getResult().save(new CompoundTag()));
            tag.putString(NBTList.CONTAINER, pending.getContainer().toJson().toString());
        }
        if (blockEntity instanceof IExpGiver expGiver) {
            tag.putInt(NBTList.EXP, expGiver.getExp());
        }
        return origin;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return;
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (blockEntity instanceof IPendingResult pending) {
                if (tag.contains(NBTList.RESULT)) pending.setResult(ItemStack.of(tag.getCompound(NBTList.RESULT)));
                if (tag.contains(NBTList.CONTAINER)) pending.setContainer(Ingredient.fromJson(JsonParser.parseString(tag.getString(NBTList.CONTAINER))));
            }
            if (blockEntity instanceof IExpGiver expGiver) {
                if (tag.contains(NBTList.EXP)) expGiver.setExp(tag.getInt(NBTList.EXP));
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        var stacks = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        for (var item : stacks) {
            if (item.is(asItem())) {
                CompoundTag tag = item.getOrCreateTag();
                if (blockEntity instanceof IPendingResult pending) {
                    tag.put(NBTList.RESULT, pending.getResult().save(new CompoundTag()));
                    tag.putString(NBTList.CONTAINER, pending.getContainer().toJson().toString());
                }
                if (blockEntity instanceof IExpGiver expGiver) {
                    tag.putInt(NBTList.EXP, expGiver.getExp());
                }
            }
        }
        return stacks;
    }

}
