package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class StatsHud {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int fps = 0;
    private int entityCount = 0;
    private int particleCount = 0;
    private int culledEntities = 0;
    private int culledBlockEntities = 0;

    public void register() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            if (!ModConfig.showFpsHud) return;

            updateStats();

            int x = 5;
            int y = 5;

            context.fill(x - 2, y - 2, x + 190, y + 74, 0x80000000);

            draw(context, "§6OptiForge", x, y, 0xFFFFFF);
            y += 12;

            String fpsColor = fps >= 60 ? "§a" : fps >= 30 ? "§e" : "§c";
            draw(context, fpsColor + "FPS: " + fps, x, y, 0xFFFFFF);
            y += 12;

            draw(context, "§bСущности: " + entityCount + " §7(скрыто: " + culledEntities + ")", x, y, 0xFFFFFF);
            y += 12;

            draw(context, "§dЧастицы: " + particleCount, x, y, 0xFFFFFF);
            y += 12;

            draw(context, "§eБлок-энтити скрыто: " + culledBlockEntities, x, y, 0xFFFFFF);
            y += 12;
            draw(context, "§7Профиль: " + HardwareDetector.getTierName() + " §8(" + PerfMode.tierName(HardwareDetector.tier) + ")", x, y, 0xFFFFFF);
        });
    }

    private void updateStats() {
        if (OptiForgeMod.entityOptimizer != null && OptiForgeMod.entityOptimizer.isScanned()) {
            entityCount = OptiForgeMod.entityOptimizer.getLastLivingCount();
        }
        if (OptiForgeMod.entityOptimizer != null) culledEntities = OptiForgeMod.entityOptimizer.getCulledCount();
        if (OptiForgeMod.particleOptimizer != null) particleCount = OptiForgeMod.particleOptimizer.getTotalParticles();
        if (OptiForgeMod.blockEntityOptimizer != null) culledBlockEntities = OptiForgeMod.blockEntityOptimizer.getCulledCount();
        parseFps();
    }

    private void parseFps() {
        try {
            fps = mc.getCurrentFps();
        } catch (Exception ignored) {
        }
    }

    private void draw(DrawContext context, String text, int x, int y, int color) {
        context.drawTextWithShadow(mc.textRenderer, text, x, y, color);
    }
}
