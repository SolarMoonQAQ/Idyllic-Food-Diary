package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.api.IBlockEntityAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements IBlockEntityAnimation {

    private Map<String, AnimTicker> animTickers = new HashMap<>();
    private BlockEntity be = (BlockEntity) (Object) this;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(BlockEntityType p_155228_, BlockPos p_155229_, BlockState p_155230_, CallbackInfo ci) {
        AnimHelper.createMap(be, AnimHelper.Fluid.IDENTIFIER);
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    public void save(CompoundTag tag, CallbackInfo ci) {
        ListTag listTag = new ListTag();
        getAnimTickers().forEach((name, anim) -> {
            CompoundTag t = new CompoundTag();
            t.putString("Name", name);
            t.put("AnimTicker", anim.serializeNBT());
            listTag.add(t);
        });
        tag.put("AnimTickers", listTag);
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void load(CompoundTag tag, CallbackInfo ci) {
        ListTag listTag = tag.getList("AnimTickers", ListTag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag t = listTag.getCompound(i);
            String name = t.getString("Name");
            AnimTicker anim = AnimTicker.of(t.getCompound("AnimTicker"));
            getAnimTickers().put(name, anim);
        }
    }

    @NotNull
    @Override
    public Map<String, AnimTicker> getAnimTickers() {
        return animTickers;
    }

    @Override
    public void setAnimTickers(@NotNull Map<String, AnimTicker> animTickers) {
        this.animTickers = animTickers;
    }

}
