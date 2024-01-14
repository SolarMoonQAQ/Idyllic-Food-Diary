package cn.solarmoon.immersive_delight.api.client.ItemRenderer;

import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * 应用于mixin
 * 接入后实现自定义模型渲染
 */
public interface ICustomItemRendererProvider extends ItemLike {

    Supplier<ItemStackRenderer> getRendererFactory();

}
