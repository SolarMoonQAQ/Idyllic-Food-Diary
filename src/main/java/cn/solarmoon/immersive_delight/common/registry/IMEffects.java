package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.BaseObjectRegistry;
import cn.solarmoon.immersive_delight.common.effect.FleetOfFootEffect;
import cn.solarmoon.immersive_delight.common.effect.RefreshmentEffect;
import cn.solarmoon.immersive_delight.common.effect.SnugEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

public class IMEffects extends BaseObjectRegistry<MobEffect> {

    //药水效果
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);

    public IMEffects() {
        super(EFFECTS);
    }

    public static final RegistryObject<MobEffect> REFRESHMENT = EFFECTS.register("refreshment", RefreshmentEffect::new);

    public static final RegistryObject<MobEffect> FLEET_OF_FOOT = EFFECTS.register("fleet_of_foot", FleetOfFootEffect::new);

    public static final RegistryObject<MobEffect> Snug = EFFECTS.register("snug", SnugEffect::new);

}
