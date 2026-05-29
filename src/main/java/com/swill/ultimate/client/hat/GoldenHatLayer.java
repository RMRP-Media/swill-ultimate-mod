package com.swill.ultimate.client.hat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.swill.ultimate.client.tariff.TariffManager;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class GoldenHatLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("swill_ultimate:textures/entity/golden_hat.png");
    private final GoldenHatModel hatModel;

    public GoldenHatLayer(PlayerModel<AbstractClientPlayer> parent, GoldenHatModel hatModel) {
        super(parent);
        this.hatModel = hatModel;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.isInvisible()) return;
        if (!"ULTIMATE".equals(TariffManager.getCurrentTariff())) return;

        float opacity = 0.75f;
        long time = System.currentTimeMillis();
        float shimmer = (float)(time % 2000) / 2000f * (float)Math.PI * 2 * 2f;
        float r = 1f;
        float g = 0.7f + 0.2f * (float)Math.sin(shimmer);
        float b = 0.2f + 0.15f * (float)Math.sin(shimmer * 1.7f);
        r += 0.1f * (float)Math.sin(shimmer * 2.3f);
        r = Math.min(1f, r); g = Math.min(1f, g); b = Math.min(1f, b);

        poseStack.pushPose();
        poseStack.translate(0, Math.sin(ageInTicks * 2f) * 0.01, 0);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        hatModel.renderToBuffer(poseStack, consumer, packedLight, 0, r, g, b, opacity);
        poseStack.popPose();
    }
}