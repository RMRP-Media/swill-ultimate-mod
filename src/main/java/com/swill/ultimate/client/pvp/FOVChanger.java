package com.swill.ultimate.client.pvp;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class FOVChanger {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && (client.player.isSprinting() || client.player.swinging)) {
                client.options.fov().set(90f);
            } else {
                client.options.fov().set(70f);
            }
        });
    }
}