package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.registry.BaseObjectRegistry;
import cn.solarmoon.immersive_delight.common.entities.DurianEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

public class IMEntityTypes extends BaseObjectRegistry<EntityType<?>> {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);

    public IMEntityTypes() {
        super(ENTITY_TYPES);
    }

    public static final RegistryObject<EntityType<DurianEntity>> DURIAN_ENTITY = ENTITY_TYPES.register("durian", () -> EntityType.Builder
            .<DurianEntity>of(DurianEntity::new, MobCategory.MISC)
            .sized(0.8F, 0.8F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(ImmersiveDelight.MOD_ID + "durian")
    );

}
