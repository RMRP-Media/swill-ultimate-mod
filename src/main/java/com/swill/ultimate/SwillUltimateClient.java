package com.swill.ultimate.client;

import com.swill.ultimate.client.hat.GoldenHatLayer;
import com.swill.ultimate.client.hat.GoldenHatModel;
import com.swill.ultimate.client.pulse.AudioPulseHandler;
import com.swill.ultimate.client.pvp.FOVChanger;
import com.swill.ultimate.client.pvp.ReachDisplay;
import com.swill.ultimate.client.pvp.TargetHighlight;
import com.swill.ultimate.client.ui.MinimalHUD;
import com.swill.ultimate.client.tariff.TariffManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class SwillUltimateClient implements ClientModInitializer {
    public static final ModelLayerLocation GOLDEN_HAT_LAYER = new ModelLayerLocation(
        new ResourceLocation(SwillUltimateMod.MOD_ID, "golden_hat"), "main"
    );
    private static KeyMapping openTariffKey;

    @Override
    public void onInitializeClient() {
        // Регистрация модели шляпы
        EntityModelLayerRegistry.registerModelLayer(GOLDEN_HAT_LAYER, GoldenHatModel::createLayer);

        // Регистрация слоя шляпы для игроков
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerRenderer playerRenderer) {
                GoldenHatModel hatModel = new GoldenHatModel(context.getModelSet().bakeLayer(GOLDEN_HAT_LAYER));
                registrationHelper.register(new GoldenHatLayer(playerRenderer, hatModel));
            }
        });

        // Клавиша P для открытия меню тарифов
        openTariffKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.swill_ultimate.open_tariff",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "category.swill_ultimate"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openTariffKey.consumeClick()) {
                if (client.player != null) {
                    TariffManager.openTariffScreen(client);
                }
            }
        });

        // Аудио-пульс
        AudioPulseHandler.init();

        // PvP
        TargetHighlight.register();
        FOVChanger.register();
        ReachDisplay.register();

        // HUD
        MinimalHUD.register();

        SwillUltimateMod.LOGGER.info("[SWILL] Client components initialized");
    }
}