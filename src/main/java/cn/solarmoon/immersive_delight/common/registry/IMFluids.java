package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.core.BaseFluidRegistry;
import cn.solarmoon.immersive_delight.common.fluid.*;
import cn.solarmoon.immersive_delight.common.fluid.no_bucket.CangShuMuttonSoupFluid;
import cn.solarmoon.immersive_delight.common.fluid.no_bucket.MushroomStewFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;


public class IMFluids {

    public static class specialRegister extends BaseFluidRegistry {

        //流体
        //不知道为什么没实现
        public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);
        public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MOD_ID);

        public specialRegister() {
            super(FLUIDS, FLUID_TYPES);
        }

    }

    public static List<FluidType> FluidTypes = new ArrayList<>(List.of(
            HotWater.FLUID_TYPE.get(),
            BlackTea.FLUID_TYPE.get(),
            GreenTea.FLUID_TYPE.get(),
            MilkTea.FLUID_TYPE.get(),
            HotMilk.FLUID_TYPE.get()
    ));

    public static List<FluidType> HotFluidTypes = new ArrayList<>(List.of(
            HotMilk.FLUID_TYPE.get(),
            HotWater.FLUID_TYPE.get()
    ));

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class HotWater {

        private static final String ID = "hot_water";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, HotWaterFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", HotWaterFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, HotWaterFluid.FluidBlock::new);
        public static RegistryObject<Item> BUCKET = IMItems.ITEMS.register(ID + "_bucket", HotWaterFluid.Bucket::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });


    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BlackTea {

        private static final String ID = "black_tea";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, BlackTeaFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", BlackTeaFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, BlackTeaFluid.FluidBlock::new);
        public static RegistryObject<Item> BUCKET = IMItems.ITEMS.register(ID + "_bucket", BlackTeaFluid.Bucket::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class GreenTea {

        private static final String ID = "green_tea";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, GreenTeaFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", GreenTeaFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, GreenTeaFluid.FluidBlock::new);
        public static RegistryObject<Item> BUCKET = IMItems.ITEMS.register(ID + "_bucket", GreenTeaFluid.Bucket::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MilkTea {

        private static final String ID = "milk_tea";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, MilkTeaFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", MilkTeaFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, MilkTeaFluid.FluidBlock::new);
        public static RegistryObject<Item> BUCKET = IMItems.ITEMS.register(ID + "_bucket", MilkTeaFluid.Bucket::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class HotMilk {

        private static final String ID = "hot_milk";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, HotMilkFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", HotMilkFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, HotMilkFluid.FluidBlock::new);
        public static RegistryObject<Item> BUCKET = IMItems.ITEMS.register(ID + "_bucket", HotMilkFluid.Bucket::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MushroomStew {

        private static final String ID = "mushroom_stew_fluid";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, MushroomStewFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", MushroomStewFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, HotWaterFluid.FluidBlock::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CangShuMuttonSoup {

        private static final String ID = "cangshu_mutton_soup_fluid";
        private static final ResourceLocation SPRITE_FLOWING = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_side");
        private static final ResourceLocation SPRITE_STILL = new ResourceLocation(MOD_ID + ":fluid/" + ID + "_fluid_top");
        public static RegistryObject<FlowingFluid> FLUID_STILL = specialRegister.FLUIDS.register(ID, CangShuMuttonSoupFluid.Source::new);
        public static RegistryObject<FlowingFluid> FLUID_FLOWING = specialRegister.FLUIDS.register(ID + "_flowing", CangShuMuttonSoupFluid.Flowing::new);
        public static RegistryObject<LiquidBlock> FLUID_BLOCK = IMBlocks.BLOCKS.register(ID, CangShuMuttonSoupFluid.FluidBlock::new);
        public static RegistryObject<FluidType> FLUID_TYPE = specialRegister.FLUID_TYPES.register(ID, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return SPRITE_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return SPRITE_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });

    }

}
