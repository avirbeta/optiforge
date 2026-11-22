package com.vizor.optiforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {

    public SettingsScreen() {
        super(Text.literal("Настройки OptiForge"));
    }

    private void rebuild() {
        MinecraftClient.getInstance().setScreen(new SettingsScreen());
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int btnWidth = 280;
        int btnHeight = 20;
        int y = 28;

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§a§l⚡ ОПРЕДЕЛИТЬ НАСТРОЙКИ ПОД МОЁ ЖЕЛЕЗО"),
                button -> {
                    OptiForgeMod.applyAutoDetectNow();
                    rebuild();
                }).dimensions(centerX - btnWidth / 2, y, btnWidth, 22).build());
        y += 28;

        addToggle(centerX, btnWidth, y, "Оптимизация сущностей", ModConfig.entityCulling,
                () -> ModConfig.entityCulling = !ModConfig.entityCulling);
        y += 24;

        addToggle(centerX, btnWidth, y, "Пропускать тики дальних мобов", ModConfig.skipEntityTicks,
                () -> ModConfig.skipEntityTicks = !ModConfig.skipEntityTicks);
        y += 24;

        addToggle(centerX, btnWidth, y, "Ограничить частицы", ModConfig.optimizedParticles,
                () -> ModConfig.optimizedParticles = !ModConfig.optimizedParticles);
        y += 24;

        addToggle(centerX, btnWidth, y, "Оптимизация предметов (дроп)", ModConfig.optimizeItems,
                () -> ModConfig.optimizeItems = !ModConfig.optimizeItems);
        y += 24;

        addToggle(centerX, btnWidth, y, "Оптимизация блок-энтити", ModConfig.blockEntityCulling,
                () -> ModConfig.blockEntityCulling = !ModConfig.blockEntityCulling);
        y += 24;

        addToggle(centerX, btnWidth, y, "Автоопределение при входе", ModConfig.autoDetect,
                () -> ModConfig.autoDetect = !ModConfig.autoDetect);
        y += 24;

        addToggle(centerX, btnWidth, y, "Показать HUD", ModConfig.showFpsHud,
                () -> ModConfig.showFpsHud = !ModConfig.showFpsHud);
        y += 28;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), button -> this.close())
                .dimensions(centerX - btnWidth / 2, y, btnWidth, btnHeight).build());
    }

    private void addToggle(int centerX, int width, int y, String name, boolean enabled, Runnable toggle) {
        Text label = Text.literal(name + ": " + (enabled ? "§aВКЛ" : "§cВЫКЛ"));
        this.addDrawableChild(ButtonWidget.builder(label, button -> {
            toggle.run();
            ModConfig.save();
            GameSettingsApplier.apply();
            rebuild();
        }).dimensions(centerX - width / 2, y, width, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("§6§lOptiForge §7— §fоптимизация Minecraft"),
                this.width / 2, 10, 0xFFFFFF);

        int h = this.height - 4;
        context.drawTextWithShadow(textRenderer, Text.literal("§7Железо: " + HardwareDetector.getSummary()), 4, h - 42, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("§7" + ModDetector.getOptimmodsSummary()), 4, h - 26, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("§7" + ModDetector.getCustomModsSummary()), 4, h - 10, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
