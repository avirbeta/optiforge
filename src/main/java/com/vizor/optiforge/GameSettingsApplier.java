package com.vizor.optiforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.particle.ParticlesMode;

public class GameSettingsApplier {

    public static void apply() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options == null) return;

        // Дистанция рендера
        if (ModConfig.renderDistanceOverride > 0) {
            mc.options.getViewDistance().setValue(ModConfig.renderDistanceOverride);
        }

        // Частицы
        if (ModConfig.optimizedParticles) {
            int limit = ModConfig.particleLimit;
            if (limit <= 20) mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
            else if (limit <= 60) mc.options.getParticles().setValue(ParticlesMode.DECREASED);
            else mc.options.getParticles().setValue(ParticlesMode.ALL);
        }

        // Графика
        if ("fast".equalsIgnoreCase(ModConfig.graphicsMode)) mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
        else if ("fancy".equalsIgnoreCase(ModConfig.graphicsMode)) mc.options.getGraphicsMode().setValue(GraphicsMode.FANCY);

        // FOV
        if (ModConfig.reduceFov) {
            int fov = mc.options.getFov().getValue();
            if (fov > ModConfig.maxFov) {
                mc.options.getFov().setValue(ModConfig.maxFov);
            }
        }

        try {
            mc.options.write();
        } catch (Exception ignored) {
        }
    }
}
