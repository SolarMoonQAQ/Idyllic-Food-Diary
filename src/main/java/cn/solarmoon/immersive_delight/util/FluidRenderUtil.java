package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

/**
 * 主要用于获取液体贴图模型等
 * 方法取自于Mekanism，感谢作者。
 */
public class FluidRenderUtil {

    //以下主要为渲染类
    public static final FluidRenderMap<Int2ObjectMap<Model3D>> CACHED_FLUIDS = new FluidRenderMap<>();
    public static final int STAGES = 1400;

    public static Model3D getFluidModel(FluidStack fluid, int stage) {
        if (CACHED_FLUIDS.containsKey(fluid) && CACHED_FLUIDS.get(fluid).containsKey(stage)) {
            return CACHED_FLUIDS.get(fluid).get(stage);
        }
        Model3D model = new Model3D();
        model.setTexture(FluidRenderMap.getFluidTexture(fluid, FluidRenderMap.FluidFlow.STILL));
        if (IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid) != null) {
            double sideSpacing = 0.00625;
            double belowSpacing = 0.0625 / 4;
            model.minX = sideSpacing;
            model.minY = belowSpacing;
            model.minZ = sideSpacing;
            model.maxX = 1 - sideSpacing;
            model.maxY = 1 - belowSpacing;
            model.maxZ = 1 - sideSpacing;
        }
        if (CACHED_FLUIDS.containsKey(fluid)) {
            CACHED_FLUIDS.get(fluid).put(stage, model);
        }
        else {
            Int2ObjectMap<Model3D> map = new Int2ObjectOpenHashMap<>();
            map.put(stage, model);
            CACHED_FLUIDS.put(fluid, map);
        }
        return model;
    }

    /**
     * Source from MIT open source <a href="https://github.com/mekanism/Mekanism/tree/1.15x">...</a>
     * <p>
     * <a href="https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE">...</a>
     * <p>
     * 原理：获取贴图对六个面分别进行渲染
     */
    public static class Model3D {

        public double minX, minY, minZ;
        public double maxX, maxY, maxZ;
        public TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
        public boolean[] renderSides = new boolean[] { true, true, true, true, true, true, false };

        public final void setBlockBounds(double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos) {
            minX = xNeg;
            minY = yNeg;
            minZ = zNeg;
            maxX = xPos;
            maxY = yPos;
            maxZ = zPos;
        }

        public double sizeX() {
            return maxX - minX;
        }

        public double sizeY() {
            return maxY - minY;
        }

        public double sizeZ() {
            return maxZ - minZ;
        }

        public boolean shouldSideRender(Direction side) {
            return renderSides[side.ordinal()];
        }

        public void setTexture(TextureAtlasSprite tex) {
            Arrays.fill(textures, tex);
        }
    }


    /**
     * Source from MIT open source <a href="https://github.com/mekanism/Mekanism/tree/1.15x">...</a>
     * <p>
     * <a href="https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE">...</a>
     * <p>
     * 获取流体对应贴图
     */
    public static class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V> {

        public enum FluidFlow {
            STILL, FLOWING
        }

        public FluidRenderMap() {
            super(FluidHashStrategy.INSTANCE);
        }

        public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidFlow type) {
            if(fluidStack.isEmpty()) return null;
            Fluid fluid = fluidStack.getFluid();
            ResourceLocation spriteLocation;
            IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
            if (type == FluidFlow.STILL) {
                spriteLocation = fluidAttributes.getStillTexture(fluidStack);
            }
            else {
                spriteLocation = fluidAttributes.getFlowingTexture(fluidStack);
            }
            return getSprite(spriteLocation);
        }

        public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
            return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
        }

        /**
         * 获取液体颜色
         */
        public static int getColor(FluidStack fluidStack) {
            Fluid fluid = fluidStack.getFluid();
            IClientFluidTypeExtensions handler = IClientFluidTypeExtensions.of(fluid);
            return handler.getTintColor(fluidStack);
        }

        /**
         * 获取液体颜色ARGB
         */
        public static float[] getColorARGB(FluidStack fluidStack) {
            int fluidColor = getColor(fluidStack);
            float[] colorArray = new float[4];
            colorArray[0] = (fluidColor >> 16 & 0xFF) / 255.0F; //红
            colorArray[1] = (fluidColor >> 8 & 0xFF) / 255.0F;  //绿
            colorArray[2] = (fluidColor & 0xFF) / 255.0F;       //蓝
            colorArray[3] = ((fluidColor >> 24) & 0xFF) / 255F; //透明度
            return colorArray;
        }

        /**
         * 获取指定颜色ARGB
         */
        public static float[] getColorARGB(int fluidColor) {
            float[] colorArray = new float[4];
            colorArray[0] = (fluidColor >> 16 & 0xFF) / 255.0F; //红
            colorArray[1] = (fluidColor >> 8 & 0xFF) / 255.0F;  //绿
            colorArray[2] = (fluidColor & 0xFF) / 255.0F;       //蓝
            colorArray[3] = ((fluidColor >> 24) & 0xFF) / 255F; //透明度
            return colorArray;
        }

        /**
         * 光照同步
         */
        public static int calculateGlowLight(int light, FluidStack fluidStack) {
            int glow = fluidStack.getFluid().getFluidType().getLightLevel();
            int FULL_LIGHT = 0xF000F0;
            if (glow >= 15) {
                return FULL_LIGHT;
            }
            int blockLight = LightTexture.block(light);
            int skyLight = LightTexture.sky(light);
            int glowLight = LightTexture.pack(Math.max(blockLight, glow), Math.max(skyLight, glow));
            return fluidStack.isEmpty() ? light : glowLight;
        }

        /**
         * 忽略量的流体匹配
         */
        public static class FluidHashStrategy implements Hash.Strategy<FluidStack> {

            public static FluidHashStrategy INSTANCE = new FluidHashStrategy();

            @Override
            public int hashCode(FluidStack stack) {
                if (stack == null || stack.isEmpty()) {
                    return 0;
                }
                int code = 1;
                code = 31 * code + stack.getFluid().hashCode();
                if (stack.hasTag()) {
                    code = 31 * code + stack.getTag().hashCode();
                }
                return code;
            }

            @Override
            public boolean equals(FluidStack a, FluidStack b) {
                return a == null ? b == null : b != null && a.isFluidEqual(b);
            }
        }
    }

    public static class RenderResizableCuboid {

        private static final Vector3f VEC_ZERO = new Vector3f(0, 0, 0);
        private static final int U_MIN = 0;
        private static final int U_MAX = 1;
        private static final int V_MIN = 2;
        private static final int V_MAX = 3;
        protected EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();

        private static Vector3f withValue(Vector3f vector, Direction.Axis axis, float value) {
            if (axis == Direction.Axis.X) {
                return new Vector3f(value, vector.y(), vector.z());
            }
            else if (axis == Direction.Axis.Y) {
                return new Vector3f(vector.x(), value, vector.z());
            }
            else if (axis == Direction.Axis.Z) {
                return new Vector3f(vector.x(), vector.y(), value);
            }
            throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
        }

        public static double getValue(Vec3 vector, Direction.Axis axis) {
            if (axis == Direction.Axis.X) {
                return vector.x;
            }
            else if (axis == Direction.Axis.Y) {
                return vector.y;
            }
            else if (axis == Direction.Axis.Z) {
                return vector.z;
            }
            throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
        }

        /**
         * model 3d cube is the fluid
         */
        public static void renderCube(Model3D cube, PoseStack matrix, VertexConsumer buffer, int argb, int light) {
            float red = FluidRenderMap.getColorARGB(argb)[0];
            float green = FluidRenderMap.getColorARGB(argb)[1];
            float blue = FluidRenderMap.getColorARGB(argb)[2];
            float alpha = FluidRenderMap.getColorARGB(argb)[3];
            Vec3 size = new Vec3(cube.sizeX(), cube.sizeY(), cube.sizeZ());
            matrix.pushPose();
            matrix.translate(cube.minX, cube.minY, cube.minZ);
            Matrix4f matrix4f = matrix.last().pose();
            for (Direction face : Direction.values()) {
                if (cube.shouldSideRender(face)) {
                    int ordinal = face.ordinal();
                    TextureAtlasSprite sprite = cube.textures[ordinal];
                    if (sprite != null) {
                        Direction.Axis u = face.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                        Direction.Axis v = face.getAxis() == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y;
                        float other = face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? (float) getValue(size, face.getAxis()) : 0;
                        face = face.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? face : face.getOpposite();
                        Direction opposite = face.getOpposite();
                        float minU = sprite.getU0();
                        float maxU = sprite.getU1();
                        float minV = sprite.getV1();
                        float maxV = sprite.getV0();
                        double sizeU = getValue(size, u);
                        double sizeV = getValue(size, v);
                        for (int uIndex = 0; uIndex < sizeU; uIndex++) {
                            float[] baseUV = new float[] { minU, maxU, minV, maxV };
                            double addU = 1;
                            if (uIndex + addU > sizeU) {
                                addU = sizeU - uIndex;
                                baseUV[U_MAX] = baseUV[U_MIN] + (baseUV[U_MAX] - baseUV[U_MIN]) * (float) addU;
                            }
                            for (int vIndex = 0; vIndex < sizeV; vIndex++) {
                                float[] uv = Arrays.copyOf(baseUV, 4);
                                double addV = 1;
                                if (vIndex + addV > sizeV) {
                                    addV = sizeV - vIndex;
                                    uv[V_MAX] = uv[V_MIN] + (uv[V_MAX] - uv[V_MIN]) * (float) addV;
                                }
                                float[] xyz = new float[] { uIndex, (float) (uIndex + addU), vIndex, (float) (vIndex + addV) };
                                renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                                renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                            }
                        }
                    }
                }
            }
            matrix.popPose();
        }

        private static void renderPoint(Matrix4f matrix4f, VertexConsumer buffer, Direction face, Direction.Axis u, Direction.Axis v, float other, float[] uv, float[] xyz, boolean minU, boolean minV,
                                 float red, float green, float blue, float alpha, int light) {
            int uFinal = minU ? U_MIN : U_MAX;
            int vFinal = minV ? V_MIN : V_MAX;
            Vector3f vertex = withValue(VEC_ZERO, u, xyz[uFinal]);
            vertex = withValue(vertex, v, xyz[vFinal]);
            vertex = withValue(vertex, face.getAxis(), other);
            buffer.vertex(matrix4f, vertex.x(), vertex.y(), vertex.z()).color(red, green, blue, alpha).uv(uv[uFinal], uv[vFinal]).uv2(light).endVertex();
        }
    }

    /**
     * 渲染
     */
    public static class FluidTankRenderType extends RenderType {

        private FluidTankRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
            super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
        }

        public static final RenderType RESIZABLE = create(ImmersiveDelight.MOD_ID + ":resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
                        .setCullState(CULL)
                        .setLightmapState(LIGHTMAP)
                        .setWriteMaskState(COLOR_WRITE)
                        .setLightmapState(LIGHTMAP)
                        //          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .createCompositeState(true));
    }

}
