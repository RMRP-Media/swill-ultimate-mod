package com.swill.ultimate.client.pulse;

import javax.sound.sampled.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import com.swill.ultimate.client.tariff.TariffManager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Environment(EnvType.CLIENT)
public class AudioPulseHandler {
    private static TargetDataLine line;
    private static float currentStrength = 0f;
    private static long lastBeat = 0;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (line == null) {
                try {
                    AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
                    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();
                } catch (LineUnavailableException e) { e.printStackTrace(); }
            }
            if (line == null || !line.isOpen()) return;

            byte[] buffer = new byte[1024];
            int bytes = line.read(buffer, 0, buffer.length);
            if (bytes <= 0) return;

            double amp = 0;
            ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < bytes/2; i++) {
                short sample = bb.getShort();
                amp += Math.abs(sample) / 32768.0;
            }
            amp /= (bytes/2);

            long now = System.currentTimeMillis();
            if (amp > 0.12 && (now - lastBeat) > 150) {
                currentStrength = 1.0f;
                lastBeat = now;
            } else {
                currentStrength *= 0.92f;
                if (currentStrength < 0.01f) currentStrength = 0f;
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!"ULTIMATE".equals(TariffManager.getCurrentTariff())) return;
            if (currentStrength <= 0.05f) return;
            int width = drawContext.getScaledWindowWidth();
            int alpha = (int)(currentStrength * 150);
            drawContext.drawString(Minecraft.getInstance().font, "⚡ PULSE ⚡", width/2 - 40, 10, 0xFFFFAA | (alpha << 24));
        });
    }
}