package cn.solarmoon.idyllic_food_diary.compat.jade;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlockEntity;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPlateable;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.ingredient_handling.IIngredientHandlingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraftforge.items.IItemHandler;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ProgressStyle;

public class JadeUtil {

    public static void addSteamingBaseTip(ITooltip iTooltip, IEvaporationRecipe eva) {
        IElementHelper ehp = iTooltip.getElementHelper();
        if (eva.isEvaporating()) {
            iTooltip.add(ehp.text(IdyllicFoodDiary.TRANSLATOR.set("jade", "evaporating", eva.getEvaporatingAmount())));
        }
    }

    public static void addSteamingTip(ITooltip iTooltip, SteamerBlockEntity steamer) {
        IElementHelper ehp = iTooltip.getElementHelper();

        int stackAmount = steamer.getStack();
        for (int i = 0; i < stackAmount; i++) {
            int r = i + 1;
            int index = i == 0 ? iTooltip.size() : iTooltip.size() - i;
            iTooltip.add(index, IdyllicFoodDiary.TRANSLATOR.set("jade", "steamer.layer_" + r));
            if (i < steamer.getInvList().size()) {
                steamer.getInvList().get(i).getStacks().forEach(stack -> iTooltip.append(index, ehp.item(stack, 0.5f)));
            }
        }

        iTooltip.add(IdyllicFoodDiary.TRANSLATOR.set("jade", "steamer.working"));
        iTooltip.append(Component
                .literal(steamer.canWork() ? "✔" : "✖")
                .withStyle(steamer.canWork() ? ChatFormatting.GREEN : ChatFormatting.RED)
        );
    }

    public static void addIngredientHandlingResult(ITooltip iTooltip, IIngredientHandlingRecipe ih) {
        IElementHelper ehp = iTooltip.getElementHelper();
        iTooltip.add(ehp.text(
                IdyllicFoodDiary.TRANSLATOR.set("jade", "ingredient_handling")
                        .copy().append(
                                ih.hasOutput() ?
                                        Component.translatable(ih.findHandleRecipe().get().result().getDescriptionId())
                                        : IdyllicFoodDiary.TRANSLATOR.set("jade", "ingredient_handling.empty")
                        )
        ));
        if (ih.hasOutput()) iTooltip.append(ehp.item(ih.findHandleRecipe().get().result(), 0.5f));
    }

    public static void addStirFryStage(ITooltip iTooltip, IStirFryRecipe stir) {
        IElementHelper ehp = iTooltip.getElementHelper();
        iTooltip.add(ehp.text(IdyllicFoodDiary.TRANSLATOR.set("jade", "stir_fry.stage",
                        stir.isStirFrying() ? stir.getPresentStage() + 1 : 0
                )
        ));
    }

    public static void addPlatingResult(ITooltip iTooltip, IPlateable pr) {
        IElementHelper ehp = iTooltip.getElementHelper();
        iTooltip.add(ehp.text(IdyllicFoodDiary.TRANSLATOR.set("jade", "plating").copy().append(
                        pr.getResult().isEmpty() ?
                                IdyllicFoodDiary.TRANSLATOR.set("jade", "plating.empty") :
                                Component.translatable(pr.getResult().getDescriptionId())
                )
        ));
        if (!pr.getResult().isEmpty()) iTooltip.append(ehp.item(pr.getResult(), 0.5f));
    }

    public static void addByTime(int timeNow, int recipeTime, ITooltip iTooltip) {
        float scale = (float) timeNow / recipeTime;
        if (timeNow != 0) {
            IElementHelper ehp = iTooltip.getElementHelper();
            IElement progress = ehp.progress(
                    scale,
                    Component.literal(StringUtil.formatTickDuration(timeNow) + "/" + StringUtil.formatTickDuration(recipeTime)).withStyle(ChatFormatting.WHITE),
                    new ProgressStyle(),
                    BoxStyle.DEFAULT,
                    true
            );
            iTooltip.add(progress);
        }
    }

    public static void addByTimeArray(int[] times, int[] recipeTimes, IItemHandler inv, ITooltip iTooltip) {
        int n = 0;
        for (int i = 0; i < times.length; i ++) {
            if (times[i] != 0) {
                int time = times[i];
                int needTime = recipeTimes[i];
                IElementHelper ehp = iTooltip.getElementHelper();
                if (n % 2 == 0) {
                    iTooltip.add(ehp.smallItem(inv.getStackInSlot(i)));
                } else {
                    iTooltip.append(iTooltip.size() - 1, ehp.smallItem(inv.getStackInSlot(i)));
                }
                n++;
                iTooltip.append(Component.literal(StringUtil.formatTickDuration(time) + "/" + StringUtil.formatTickDuration(needTime)).withStyle(ChatFormatting.WHITE));
            }
        }
    }

}
