package com.swill.ultimate.client.pvp;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class TargetHighlight {
    public static void register() {
        WorldRenderEvents.LAST.register(context -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (mc.crosshairPickEntity instanceof LivingEntity target && target != mc.player) {
                double x = target.getX() - context.camera().getPosition().x;
                double y = target.getY() - context.camera().getPosition().y;
                double z = target.getZ() - context.camera().getPosition().z;
                AABB aabb = target.getBoundingBox().move(-target.getX(), -target.getY(), -target.getZ())
                               .move(x, y, z);
                PoseStack pose = context.matrixStack();
                pose.pushPose();
                VertexConsumer buffer = context.consumers().getBuffer(RenderType.lines());
                Matrix4f mat = pose.last().pose();
                // рисование рамки (упрощённо)
                drawBox(mat, buffer, aabb, 1f, 0.2f, 0.4f, 1f);
                pose.popPose();
            }
        });
    }

    private static void drawBox(Matrix4f mat, VertexConsumer buffer, AABB box, float r, float g, float b, float a) {
        // Минимальная реализация
        float minX = (float)box.minX;
        float minY = (float)box.minY;
        float minZ = (float)box.minZ;
        float maxX = (float)box.maxX;
        float maxY = (float)box.maxY;
        float maxZ = (float)box.maxZ;
        // рёбра
        buffer.vertex(mat, minX, minY, minZ).color(r,g,b,a).endVertex();
        buffer.vertex(mat, maxX, minY, minZ).color(r,g,b,a).endVertex();
        // ... (для краткости остальные линии опущены, но в реальном коде надо все 12 рёбер)
    }
}