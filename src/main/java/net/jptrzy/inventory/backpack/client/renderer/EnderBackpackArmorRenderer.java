package net.jptrzy.inventory.backpack.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.model.BackpackModel;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class EnderBackpackArmorRenderer implements ArmorRenderer {

    public static final Identifier BACKPACK_MODEL_TEXTURE = Main.id("textures/armor/ender_backpack.png");
    private BackpackModel model;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        BackpackModel model = getModel();
        contextModel.setAttributes(model);

        model.setAngles(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(BACKPACK_MODEL_TEXTURE), false, stack.hasGlint());
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }

    public BackpackModel getModel() {
        if (model == null) {
            model = new BackpackModel();
        }
        return model;
    }
}
