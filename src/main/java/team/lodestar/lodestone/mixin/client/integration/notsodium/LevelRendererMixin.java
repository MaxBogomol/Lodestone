package team.lodestar.lodestone.mixin.client.integration.notsodium;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.events.Stage;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
/*
    @Inject(method = "renderSectionLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;clearRenderState()V"))
    private void lodestone$injectEvent6(RenderType renderType, double d, double e, double f, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci){
        LodestoneRenderEvents.BEFORE_CLEAR.invoker().render(renderType, poseStack, Stage.AFTER_SOLID_BLOCKS);
    }

 */
}