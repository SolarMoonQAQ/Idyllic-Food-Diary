package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BlockMarker;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainParticle.class)
public abstract class TerrainParticleMixin extends TextureSheetParticle {

    protected TerrainParticleMixin(ClientLevel p_108323_, double p_108324_, double p_108325_, double p_108326_) {
        super(p_108323_, p_108324_, p_108325_, p_108326_);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
    public void init(ClientLevel p_172451_, double p_172452_, double p_172453_, double p_172454_, double p_172455_, double p_172456_, double p_172457_, BlockState state, BlockPos p_172459_, CallbackInfo ci) {
        FluidState fluidState = state.getFluidState();
        if (!fluidState.isEmpty()) {
            int fluidColor = IClientFluidTypeExtensions.of(fluidState).getTintColor();
            float[] colorArray = new float[]{(float)(fluidColor >> 16 & 255) / 255.0F, (float)(fluidColor >> 8 & 255) / 255.0F, (float)(fluidColor & 255) / 255.0F, (float)(fluidColor >> 24 & 255) / 255.0F};
            setColor(colorArray[0], colorArray[1], colorArray[2]);
        }
    }

}
