package team.lodestar.lodestone.systems.particle.render_types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;
import team.lodestar.lodestone.registry.client.*;

import java.util.function.Supplier;

public interface LodestoneScreenParticleRenderType {
    LodestoneScreenParticleRenderType ADDITIVE = new LodestoneScreenParticleRenderType() {
        @Override
        public void begin(Tesselator tesselator, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderSystem.setShader(LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(BufferBuilder pBuilder) {
            BufferUploader.drawWithShader(pBuilder.buildOrThrow());
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };
    LodestoneScreenParticleRenderType TRANSPARENT = new LodestoneScreenParticleRenderType() {
        @Override
        public void begin(Tesselator tesselator, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(BufferBuilder pBuilder) {
            BufferUploader.drawWithShader(pBuilder.buildOrThrow());
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };

    LodestoneScreenParticleRenderType LUMITRANSPARENT = new LodestoneScreenParticleRenderType() {
        @Override
        public void begin(Tesselator tesselator, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            Supplier<ShaderInstance> instance = LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance();
            RenderSystem.setShader(instance);
            instance.get().safeGetUniform("LumiTransparency").set(1f);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(BufferBuilder pBuilder) {
            BufferUploader.drawWithShader(pBuilder.buildOrThrow());
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            Supplier<ShaderInstance> instance = LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance();
            instance.get().safeGetUniform("LumiTransparency").set(0f);
        }
    };

    void begin(Tesselator tesselator, TextureManager pTextureManager);

    void end(BufferBuilder pBuilder);
}