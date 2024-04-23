package cn.solarmoon.idyllic_food_diary.common.item.equipment;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.client.humanoid_model.ConjurerHatModel;
import cn.solarmoon.idyllic_food_diary.client.registry.IMLayers;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.IItemArmorModelProvider;
import cn.solarmoon.solarmoon_core.api.common.item.equipment.BaseArmorMaterial;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BathrobeItem extends ArmorItem implements IItemArmorModelProvider {

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
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> humanoidModel) {
        return new ConjurerHatModel<>(getEntityModelSet().bakeLayer(IMLayers.BATHROBE.get()));
    }

}
