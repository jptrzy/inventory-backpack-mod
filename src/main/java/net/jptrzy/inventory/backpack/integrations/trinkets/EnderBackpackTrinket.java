package net.jptrzy.inventory.backpack.integrations.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jptrzy.inventory.backpack.client.Client;
import net.jptrzy.inventory.backpack.client.renderer.EnderBackpackArmorRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EnderBackpackTrinket extends BackpackTrinket {

    private EnderBackpackTrinket() {}

    public static final EnderBackpackTrinket INSTANCE = new EnderBackpackTrinket();

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements TrinketRenderer {

        @Override
        public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            BipedEntityModel<LivingEntity> model = Client.ENDER_BACKPACK_ARMOR_RENDERER.getModel();

            model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
            model.animateModel(entity, limbAngle, limbDistance, tickDelta);

            TrinketRenderer.followBodyRotations(entity, model);

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(EnderBackpackArmorRenderer.BACKPACK_MODEL_TEXTURE));
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        }
    }
}
