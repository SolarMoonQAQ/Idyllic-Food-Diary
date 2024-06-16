package cn.solarmoon.idyllic_food_diary.element.matter.bathrobe;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.registry.client.IMLayers;
import cn.solarmoon.solarmoon_core.api.item_util.BaseArmorMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BathrobeItem extends ArmorItem {

    public BathrobeItem() {
        super(new BaseArmorMaterial(
                IdyllicFoodDiary.MOD_ID + "bathrobe",
                512, 3, 1,0, 15,
                SoundEvents.ARMOR_EQUIP_LEATHER, Ingredient.of(ItemTags.WOOL)
                ),
                Type.CHESTPLATE,
                new Properties().stacksTo(1)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return new ConjurerHatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(IMLayers.BATHROBE.get()));
            }
        });
    }
}
