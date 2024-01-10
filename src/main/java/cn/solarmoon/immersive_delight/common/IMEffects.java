package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.effects.FleetOfFootEffect;
import cn.solarmoon.immersive_delight.common.effects.RefreshmentEffect;
import cn.solarmoon.immersive_delight.common.effects.SnugEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.EFFECTS;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMEffects {

    public static final RegistryObject<MobEffect> REFRESHMENT = EFFECTS.register("refreshment", RefreshmentEffect::new);

    public static final RegistryObject<MobEffect> FLEET_OF_FOOT = EFFECTS.register("fleet_of_foot", FleetOfFootEffect::new);

    public static final RegistryObject<MobEffect> Snug = EFFECTS.register("snug", SnugEffect::new);

}
