package com.swill.ultimate.client.ui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public class MinimalHUD {
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            int health = (int) Math.ceil(mc.player.getHealth());
            int maxHealth = (int) mc.player.getMaxHealth();
            int food = mc.player.getFoodData().getFoodLevel();
            drawContext.drawString(mc.font, "❤ " + health + "/" + maxHealth, 10, 10, 0xFF5555);
            drawContext.drawString(mc.font, "🍗 " + food, 10, 20, 0x55FF55);
        });
    }
}