package com.swill.ultimate.client.tariff;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import java.io.*;

public class TariffManager {
    private static String currentTariff = "FREE";
    private static final File CONFIG_FILE = new File("config/swill_ultimate_tariff.txt");

    static { loadTariff(); }

    public static String getCurrentTariff() { return currentTariff; }

    public static void setTariff(String tariff) {
        currentTariff = tariff;
        saveTariff();
    }

    private static void saveTariff() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            w.write(currentTariff);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void loadTariff() {
        if (CONFIG_FILE.exists()) {
            try (BufferedReader r = new BufferedReader(new FileReader(CONFIG_FILE))) {
                currentTariff = r.readLine();
                if (currentTariff == null) currentTariff = "FREE";
            } catch (IOException e) { currentTariff = "FREE"; }
        }
    }

    public static void openTariffScreen(Minecraft client) {
        client.setScreen(new TariffScreen());
    }

    private static class TariffScreen extends Screen {
        protected TariffScreen() { super(Component.literal("SWILL Tariffs")); }
        @Override
        protected void init() {
            int cx = width / 2;
            addRenderableWidget(Button.builder(Component.literal("FREE - Basic HUD"), b -> { setTariff("FREE"); onClose(); })
                .bounds(cx-100, 60, 200, 20).build());
            addRenderableWidget(Button.builder(Component.literal("PRO - PvP + FPS"), b -> { setTariff("PRO"); onClose(); })
                .bounds(cx-100, 90, 200, 20).build());
            addRenderableWidget(Button.builder(Component.literal("ULTIMATE - Hat + Pulse"), b -> { setTariff("ULTIMATE"); onClose(); })
                .bounds(cx-100, 120, 200, 20).build());
        }
        @Override
        public void onClose() { Minecraft.getInstance().setScreen(null); }
    }
}