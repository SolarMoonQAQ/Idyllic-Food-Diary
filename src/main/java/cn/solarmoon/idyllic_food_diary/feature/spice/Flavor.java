package cn.solarmoon.idyllic_food_diary.feature.spice;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCapabilities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMDamageTypes;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Flavor {

    public static final List<Flavor> ALL = new ArrayList<>();
    public static final int DEFAULT_LETHAL_DOSE = 5;
    public static final String FLAVOR = "FlavorAssessment";

    private final Spice spiceUnit;
    private final List<MobEffectInstance> effects;
    private final int lethalDose;

    public Flavor(Spice spiceUnit, List<MobEffectInstance> effects, int lethalDose) {
        this.spiceUnit = spiceUnit;
        this.effects = effects;
        this.lethalDose = lethalDose;
    }

    public Either<List<MobEffectInstance>, Consumer<LivingEntity>> outputExcessEffects(Spice spice) {
        List<MobEffectInstance> effectInstances = new ArrayList<>();
        if (new Spice(spice.getId(), spice.getType(), Spice.Step.EMPTY, spice.getAmount()).isSame(spiceUnit)) {
            Spice.Type type = spice.getType();
            int divisor = type == Spice.Type.FLUID ? Spice.FLUID_STOCK : Spice.ITEM_STOCK;
            int amplifier = (spice.getAmount() - spiceUnit.getAmount()) / divisor;
            if (amplifier < lethalDose) {
                // 正面
                effects.forEach(effect -> effectInstances.add(
                        new MobEffectInstance(
                                effect.getEffect(),
                                Math.max(effect.getDuration() - amplifier * 200, 0),
                                effect.getAmplifier() + amplifier,
                                effect.isAmbient(),
                                effect.isVisible(),
                                effect.showIcon()
                        )));
                // 负面
                effectInstances.add(new MobEffectInstance(MobEffects.CONFUSION, (amplifier + 1) * 200));
                if (amplifier >= lethalDose / 5) {
                    effectInstances.add(new MobEffectInstance(MobEffects.BLINDNESS, (amplifier - lethalDose / 5) * 100));
                }
                if (amplifier >= lethalDose * 3 / 5) {
                    effectInstances.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, amplifier - lethalDose * 3 / 5));
                }
            } else {
                return Either.right(getLethalCommand());
            }
        }
        return Either.left(effectInstances);
    }

    public static Either<List<MobEffectInstance>, Consumer<LivingEntity>> output(ItemStack stack) {
        SpiceList spicesExcess = getExcessSpices(stack);
        if (spicesExcess.size() == 2) return Either.right(FarmerUtil::tasteBudShock);
        if (spicesExcess.size() >= 4) return Either.right(getLethalCommand());
        List<MobEffectInstance> effectInstances = new ArrayList<>();
        Consumer<LivingEntity> lethalC = null;
        for (var s : ALL) {
            for (var spice : spicesExcess) {
                s.outputExcessEffects(spice).left().ifPresent(effectInstances::addAll);
                if (s.outputExcessEffects(spice).right().isPresent()) lethalC = s.outputExcessEffects(spice).right().get();
            }
        }
        return lethalC == null ? Either.left(effectInstances) : Either.right(lethalC);
    }

    public static Consumer<LivingEntity> getLethalCommand() {
        return FarmerUtil::tasteBudShock;
    }

    public static void put(Item item, List<MobEffectInstance> effects, int lethalDose) {
        ALL.add(new Flavor(Spice.createUnit(item), effects, lethalDose));
    }

    public static void put(Fluid fluid, List<MobEffectInstance> effects, int lethalDose) {
        ALL.add(new Flavor(Spice.createUnit(fluid), effects, lethalDose));
    }

    public static Optional<Flavor> get(Item item) {
        return ALL.stream().filter(f -> f.spiceUnit.isSame(Spice.createUnit(item))).findFirst();
    }

    public static Optional<Flavor> get(Fluid fluid) {
        return ALL.stream().filter(f -> f.spiceUnit.isSame(Spice.createUnit(fluid))).findFirst();
    }

    /**
     * @return 获取超出的调料表
     */
    public static SpiceList getExcessSpices(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        SpiceList spices = new SpiceList();
        if (tag != null && tag.contains(FLAVOR)) {
            ListTag listTag = tag.getList(FLAVOR, ListTag.TAG_COMPOUND);
            spices.deserializeNBT(listTag);
        }
        return spices;
    }

    /**
     * 对最终产品进行风味的评定
     */
    public static void doFlavorAssessment(SpiceList baseSpicesToMatch, ItemStack stack) {
        stack.getCapability(IMCapabilities.FOOD_ITEM_DATA).ifPresent(d -> {
            SpicesCap spicesCap = d.getSpicesData();
            SpiceList spices = spicesCap.getSpices();
            SpiceList excessList = new SpiceList();
            for (Spice spice : spices) {
                Spice match = baseSpicesToMatch.stream().filter(s -> s.isSame(spice)).findFirst().orElse(Spice.EMPTY);
                int excess = spice.getAmount() - match.getAmount();
                excessList.add(spice.copyWithCount(excess));
            }
            stack.getOrCreateTag().put(FLAVOR, excessList.serializeNBT());
        });
    }

    @Override
    public String toString() {
        return "Flavor{" +
                "spiceUnit=" + spiceUnit +
                ", effects=" + effects +
                ", lethalDose=" + lethalDose +
                '}';
    }

}
