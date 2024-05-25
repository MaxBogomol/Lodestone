package team.lodestar.lodestone.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

import java.lang.invoke.MethodHandles;

public interface LodestoneRenderEvents {


    Event<AfterWeather> AFTER_WEATHER = EventFactory.createArrayBacked(AfterWeather.class, (poseStack, partialTick, stage) -> { }, callbacks -> (poseStack, partialTick, stage)  -> {
        for (final AfterWeather callback : callbacks) {
            callback.render(poseStack, partialTick, Stage.AFTER_WEATHER);
        }
    });

    Event<AfterParticles> AFTER_PARTICLES = EventFactory.createArrayBacked(AfterParticles.class, (poseStack, partialTick, stage) -> { }, callbacks -> (poseStack, partialTick, stage) -> {
        for (final AfterParticles callback : callbacks) {
            callback.render(poseStack, partialTick, Stage.AFTER_PARTICLES);
        }
    });

    Event<AfterSky> AFTER_SKY = EventFactory.createArrayBacked(AfterSky.class, (poseStack, partialTick, stage) -> { }, callbacks -> (poseStack, partialTick, stage) -> {
        for (final AfterSky callback : callbacks) {
            callback.render(poseStack, partialTick, Stage.AFTER_SKY);
        }
    });

    Event<BeforeClear> BEFORE_CLEAR = EventFactory.createArrayBacked(BeforeClear.class, (poseStack, partialTick, stage) -> { }, callbacks -> (poseStack, partialTick, stage) -> {
        for (final BeforeClear callback : callbacks) {
            callback.render(poseStack, partialTick, Stage.AFTER_SKY);
        }
    });

    @FunctionalInterface
    interface AfterWeather {
        void render(PoseStack poseStack, float partialTick, Stage stage);
    }

    @FunctionalInterface
    interface AfterParticles {
        void render(PoseStack poseStack, float partialTick, Stage stage);
    }

    @FunctionalInterface
    interface AfterSky {
        void render(PoseStack poseStack, float partialTick, Stage stage);
    }

    @FunctionalInterface
    interface BeforeClear {
        void render(RenderType renderType, PoseStack poseStack, Stage stage);
    }
}