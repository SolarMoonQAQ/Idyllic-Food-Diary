package cn.solarmoon.idyllic_food_diary.element.matter.cookware;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IExpGiver;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPendingResult;
import cn.solarmoon.solarmoon_core.api.block_base.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
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

/**
 * 将保存basic feature的内容，如exp，预存物给予器等
 */
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
            tag.put(IPendingResult.RESULT, pending.getResult().save(new CompoundTag()));
            tag.putString(IPendingResult.CONTAINER, pending.getContainer().toJson().toString());
        }
        if (blockEntity instanceof IExpGiver expGiver) {
            tag.putInt(IExpGiver.EXP, expGiver.getExp());
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
                if (tag.contains(IPendingResult.RESULT)) pending.setResult(ItemStack.of(tag.getCompound(IPendingResult.RESULT)));
                if (tag.contains(IPendingResult.CONTAINER)) pending.setContainer(Ingredient.fromJson(JsonParser.parseString(tag.getString(IPendingResult.CONTAINER))));
            }
            if (blockEntity instanceof IExpGiver expGiver) {
                if (tag.contains(IExpGiver.EXP)) expGiver.setExp(tag.getInt(IExpGiver.EXP));
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
                    tag.put(IPendingResult.RESULT, pending.getResult().save(new CompoundTag()));
                    tag.putString(IPendingResult.CONTAINER, pending.getContainer().toJson().toString());
                }
                if (blockEntity instanceof IExpGiver expGiver) {
                    tag.putInt(IExpGiver.EXP, expGiver.getExp());
                }

            }
        }
        return stacks;
    }

}
