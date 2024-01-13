package cn.solarmoon.immersive_delight.common;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.entities.DurianEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ENTITY_TYPES;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMEntityTypes {

    public static final RegistryObject<EntityType<DurianEntity>> DURIAN_ENTITY = ENTITY_TYPES.register("durian", () -> EntityType.Builder
            .<DurianEntity>of(DurianEntity::new, MobCategory.MISC)
            .sized(0.8F, 0.8F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(ImmersiveDelight.MOD_ID + "durian")
    );

}
