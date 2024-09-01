package team.lodestar.lodestone.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.client.ClientTickCounter;


@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public abstract boolean isPaused();


    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow public abstract RenderTarget getMainRenderTarget();

    @Shadow @Final private DeltaTracker.Timer timer;

    @Shadow public abstract long getFrameTimeNs();

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(Lnet/minecraft/client/DeltaTracker;Z)V"))
    private void onFrameStart(boolean tick, CallbackInfo ci) {
        ClientTickCounter.renderTick(isPaused() ? timer.getGameTimeDeltaTicks() : getFrameTimeNs());
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V", ordinal = 17))
    private void lodestone$registerParticleFactories(GameConfig gameConfig, CallbackInfo ci) {
        LodestoneScreenParticleRegistry.registerParticleFactory();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 4,  shift = At.Shift.AFTER))
    private void lodestone$renderTickThingamajig(boolean tick, CallbackInfo ci) {
        ScreenParticleHandler.renderTick();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lodestone$onInit(GameConfig gameConfig, CallbackInfo ci) {
        RenderHandler.LODESTONE_DEPTH_CACHE = new TextureTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height, true, Minecraft.ON_OSX);

        //RenderHandler.TEMP_RENDER_TARGET = new TextureTarget(getMainRenderTarget().width, getMainRenderTarget().height, true, Minecraft.ON_OSX);;

    }
}