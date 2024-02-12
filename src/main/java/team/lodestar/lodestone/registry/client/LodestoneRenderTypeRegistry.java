package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeData;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import javax.annotation.*;
import java.util.HashMap;
import java.util.function.*;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.*;
import static team.lodestar.lodestone.handlers.RenderHandler.LARGER_BUFFER_SOURCES;

public class LodestoneRenderTypeRegistry extends RenderStateShard {

    public static final Runnable TRANSPARENT_FUNCTION = () -> RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final Runnable ADDITIVE_FUNCTION = () -> RenderSystem.blendFunc(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

    public static final EmptyTextureStateShard NO_TEXTURE = RenderStateShard.NO_TEXTURE;
    public static final LightmapStateShard LIGHTMAP = RenderStateShard.LIGHTMAP;
    public static final LightmapStateShard NO_LIGHTMAP = RenderStateShard.NO_LIGHTMAP;
    public static final CullStateShard CULL = RenderStateShard.CULL;
    public static final CullStateShard NO_CULL = RenderStateShard.NO_CULL;

    public LodestoneRenderTypeRegistry(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    /**
     * Stores many copies of render types, a copy is a new instance of a render type with the same properties.
     * It's useful when we want to apply different uniform changes with each separate use of our render type.
     * Use the {@link #copyAndStore(Object, RenderType)} {@link #copy(RenderType)} methods to create copies.
     */
    public static final HashMap<Pair<Object, RenderType>, RenderType> COPIES = new HashMap<>();

    public static final Function<RenderTypeData, RenderType> GENERIC = (data) -> createGenericRenderType(data.name, data.format, data.mode, data.shader, data.transparency, data.texture, data.cull);

    private static Consumer<LodestoneCompositeStateBuilder> MODIFIER;
    /**
     * Static, one off Render Types. Should be self-explanatory.
     */

    public static final RenderType ADDITIVE_PARTICLE = createGenericRenderType("lodestone:additive_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_PARTICLES)
            .setCullState(NO_CULL));

    public static final RenderType ADDITIVE_BLOCK_PARTICLE = createGenericRenderType("lodestone:additive_block_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS)
            .setCullState(NO_CULL));

    public static final RenderType ADDITIVE_BLOCK = createGenericRenderType("lodestone:additive_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS));

    public static final RenderType ADDITIVE_SOLID = createGenericRenderType("lodestone:additive_block", POSITION_COLOR_LIGHTMAP, QUADS, builder()
            .setShaderState(POSITION_COLOR_LIGHTMAP_SHADER)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(NO_TEXTURE));

    public static final RenderType TRANSPARENT_PARTICLE = createGenericRenderType("lodestone:transparent_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_PARTICLES)
            .setCullState(NO_CULL));

    public static final RenderType TRANSPARENT_BLOCK_PARTICLE = createGenericRenderType("lodestone:transparent_block_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS)
            .setCullState(NO_CULL));

    public static final RenderType TRANSPARENT_BLOCK = createGenericRenderType("lodestone:transparent_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS));

    public static final RenderType TRANSPARENT_SOLID = createGenericRenderType("lodestone:transparent_block", POSITION_COLOR_LIGHTMAP, QUADS, builder()
            .setShaderState(POSITION_COLOR_LIGHTMAP_SHADER)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(NO_TEXTURE));

    public static final RenderType LUMITRANSPARENT_PARTICLE = copyWithUniformChanges("lodestone:lumitransparent_particle", TRANSPARENT_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);
    public static final RenderType LUMITRANSPARENT_BLOCK_PARTICLE = copyWithUniformChanges("lodestone:lumitransparent_block_particle", TRANSPARENT_BLOCK_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);
    public static final RenderType LUMITRANSPARENT_BLOCK = copyWithUniformChanges("lodestone:lumitransparent_block", TRANSPARENT_BLOCK, ShaderUniformHandler.LUMITRANSPARENT);
    public static final RenderType LUMITRANSPARENT_SOLID = copyWithUniformChanges("lodestone:lumitransparent_solid", TRANSPARENT_SOLID, ShaderUniformHandler.LUMITRANSPARENT);

    /**
     * Render Functions. You can create Render Types by statically applying these to your texture. Alternatively, use {@link #GENERIC} if none of the presets suit your needs.
     * For Static Definitions use {@link RenderTypeProvider#apply(ResourceLocation)}, otherwise use {@link RenderTypeProvider#applyAndCache(ResourceLocation)}
     */
    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":texture", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
                    .setTransparencyState(StateShards.NO_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":transparent_texture", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
                    .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":transparent_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.TRIANGLE_TEXTURE)
                    .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));
    public static final RenderTypeProvider TRANSPARENT_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":transparent_scrolling_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.SCROLLING_TRIANGLE_TEXTURE)
                    .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":additive_texture", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
                    .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));
    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":additive_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.TRIANGLE_TEXTURE)
                    .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));
    public static final RenderTypeProvider ADDITIVE_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) ->
            createGenericRenderType(texture.getNamespace() + ":additive_scrolling_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(LodestoneShaderRegistry.SCROLLING_TRIANGLE_TEXTURE)
                    .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setCullState(CULL)
                    .setTextureState(texture)));


    /**
     * &#064;Deprecated - use @Link{LodestoneRenderTypeRegistry}
     */
    @Deprecated
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, ShaderStateShard shader, TransparencyStateShard transparency, ResourceLocation texture) {
        return createGenericRenderType(modId, name, format, QUADS, shader, transparency, new TextureStateShard(texture, false, false), CULL);
    }

    @Deprecated
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture, CullStateShard cull) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, texture, cull);
    }

    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture, CullStateShard cull) {
        return createGenericRenderType(name, format, mode, builder()
                .setShaderState(shader)
                .setTransparencyState(transparency)
                .setTextureState(texture)
                .setLightmapState(LIGHTMAP)
                .setCullState(cull));
    }

    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, LodestoneCompositeStateBuilder builder) {
        return createGenericRenderType(name, format, mode, builder, null);
    }

    /**
     * Creates a custom render type and creates a buffer builder for it.
     */
    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, LodestoneCompositeStateBuilder builder, ShaderUniformHandler handler) {
        int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
        if (MODIFIER != null) {
            MODIFIER.accept(builder);
        }
        RenderType type = RenderType.create(name, format, builder.mode != null ? builder.mode : mode, size, false, false, builder.createCompositeState(true));
        RenderHandler.addRenderType(type);
        if (handler != null) {
            applyUniformChanges(type, handler);
        }
        MODIFIER = null;
        return type;
    }

    public static RenderType copyWithUniformChanges(RenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copy(type), handler);
    }

    public static RenderType copyWithUniformChanges(String newName, RenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copy(newName, type), handler);
    }

    /**
     * Queues shader uniform changes for a render type. When we end batches in {@link RenderHandler}}, we do so one render type at a time.
     * Prior to ending a batch, we run {@link ShaderUniformHandler#updateShaderData(ShaderInstance)} if one is present for a given render type.
     */
    public static RenderType applyUniformChanges(RenderType type, ShaderUniformHandler handler) {
        RenderHandler.UNIFORM_HANDLERS.put(type, handler);
        return type;
    }

    /**
     * Creates a copy of a render type.
     */
    public static RenderType copy(RenderType type) {
        return GENERIC.apply(new RenderTypeData((RenderType.CompositeRenderType) type));
    }

    public static RenderType copy(String newName, RenderType type) {
        return GENERIC.apply(new RenderTypeData(newName, (RenderType.CompositeRenderType) type));
    }

    /**
     * Creates a copy of a render type and stores it in the {@link #COPIES} hashmap, with the key being a pair of original render type and copy index.
     */
    public static RenderType copyAndStore(Object index, RenderType type) {
        return COPIES.computeIfAbsent(Pair.of(index, type), (p) -> GENERIC.apply(new RenderTypeData((RenderType.CompositeRenderType) type)));
    }

    public static void addRenderTypeModifier(Consumer<LodestoneCompositeStateBuilder> modifier) {
        MODIFIER = modifier;
    }

    public static LodestoneCompositeStateBuilder builder() {
        return new LodestoneCompositeStateBuilder().setLightmapState(LIGHTMAP);
    }

    public static class LodestoneCompositeStateBuilder extends RenderType.CompositeState.CompositeStateBuilder {

        protected VertexFormat.Mode mode;

        LodestoneCompositeStateBuilder() {
            super();
        }

        public LodestoneCompositeStateBuilder replaceVertexFormat(VertexFormat.Mode mode) {
            this.mode = mode;
            return this;
        }

        public LodestoneCompositeStateBuilder setTextureState(ResourceLocation texture) {
            return setTextureState(new RenderStateShard.TextureStateShard(texture, false, false));
        }

        public LodestoneCompositeStateBuilder setShaderState(ShaderHolder shaderHolder) {
            return setShaderState(shaderHolder.getShard());
        }

        @Override
        public LodestoneCompositeStateBuilder setTextureState(EmptyTextureStateShard pTextureState) {
            return (LodestoneCompositeStateBuilder) super.setTextureState(pTextureState);
        }

        @Override
        public LodestoneCompositeStateBuilder setShaderState(ShaderStateShard pShaderState) {
            return (LodestoneCompositeStateBuilder) super.setShaderState(pShaderState);
        }

        @Override
        public LodestoneCompositeStateBuilder setTransparencyState(TransparencyStateShard pTransparencyState) {
            return (LodestoneCompositeStateBuilder) super.setTransparencyState(pTransparencyState);
        }

        @Override
        public LodestoneCompositeStateBuilder setDepthTestState(DepthTestStateShard pDepthTestState) {
            return (LodestoneCompositeStateBuilder) super.setDepthTestState(pDepthTestState);
        }

        @Override
        public LodestoneCompositeStateBuilder setCullState(CullStateShard pCullState) {
            return (LodestoneCompositeStateBuilder) super.setCullState(pCullState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLightmapState(LightmapStateShard pLightmapState) {
            return (LodestoneCompositeStateBuilder) super.setLightmapState(pLightmapState);
        }

        @Override
        public LodestoneCompositeStateBuilder setOverlayState(OverlayStateShard pOverlayState) {
            return (LodestoneCompositeStateBuilder) super.setOverlayState(pOverlayState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLayeringState(LayeringStateShard pLayerState) {
            return (LodestoneCompositeStateBuilder) super.setLayeringState(pLayerState);
        }

        @Override
        public LodestoneCompositeStateBuilder setOutputState(OutputStateShard pOutputState) {
            return (LodestoneCompositeStateBuilder) super.setOutputState(pOutputState);
        }

        @Override
        public LodestoneCompositeStateBuilder setTexturingState(TexturingStateShard pTexturingState) {
            return (LodestoneCompositeStateBuilder) super.setTexturingState(pTexturingState);
        }

        @Override
        public LodestoneCompositeStateBuilder setWriteMaskState(WriteMaskStateShard pWriteMaskState) {
            return (LodestoneCompositeStateBuilder) super.setWriteMaskState(pWriteMaskState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLineState(LineStateShard pLineState) {
            return (LodestoneCompositeStateBuilder) super.setLineState(pLineState);
        }
    }
}