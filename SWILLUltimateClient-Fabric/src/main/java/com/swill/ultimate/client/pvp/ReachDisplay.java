package com.swill.ultimate.client.pvp;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public class ReachDisplay {
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.crosshairPickEntity != null) {
                double dist = mc.player.distanceTo(mc.crosshairPickEntity);
                drawContext.drawString(mc.font, String.format("⚔️ %.1f ⚔️", dist), 10, 30, 0xFFFFFF);
            }
        });
    }
}