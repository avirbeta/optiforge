package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class FpsMeter {

    private long lastSampleTime = 0;
    private int frames = 0;
    public int currentFps = 0;
    private long windowStart = 0;
    private int windowFrames = 0;
    public int averageFps = 0;

    public void register() {
        // Считаем кадры, а не тики: HudRenderCallback вызывается на каждый кадр.
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> tick());
    }

    public void tick() {
        long now = System.currentTimeMillis();
        frames++;
        windowFrames++;

        if (lastSampleTime == 0) lastSampleTime = now;
        long elapsed = now - lastSampleTime;
        if (elapsed >= 1000) {
            currentFps = (int) ((frames * 1000L) / elapsed);
            frames = 0;
            lastSampleTime = now;
        }

        if (windowStart == 0) windowStart = now;
        long windowElapsed = now - windowStart;
        if (windowElapsed >= 3000) {
            averageFps = (int) ((windowFrames * 1000L) / windowElapsed);
            windowFrames = 0;
            windowStart = now;
        }
    }

    public int getCurrentFps() {
        if (currentFps > 0) return currentFps;
        // Запасной вариант: берём FPS из отладочной строки Minecraft.
        return readDebugFps();
    }

    public int getAverageFps() {
        return averageFps > 0 ? averageFps : getCurrentFps();
    }

    public static int readDebugFps() {
        try {
            return MinecraftClient.getInstance().getCurrentFps();
        } catch (Exception ignored) {
            return 0;
        }
    }
}
