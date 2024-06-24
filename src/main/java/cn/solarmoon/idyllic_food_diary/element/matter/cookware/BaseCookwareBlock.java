package cn.solarmoon.idyllic_food_diary.element.matter.cookware;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IExpGiver;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPendingResult;
import cn.solarmoon.solarmoon_core.api.block_base.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import com.google.gson.JsonParser;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
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

public abstract class BaseCookwareBlock extends SyncedEntityBlock implements IHorizontalFacingBlock {

    public BaseCookwareBlock(Properties properties) {
        super(properties);
    }

}
