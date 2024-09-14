package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import net.minecraft.world.item.Item
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Tool

class SpatulaItem(
    tier: Tier = Tiers.IRON,
    properties: Properties = Properties().attributes(createAttributes(Tiers.IRON, 1, -2.4f)),
    toolComponentData: Tool = createToolProperties()
): SwordItem(tier, properties, toolComponentData) {
}