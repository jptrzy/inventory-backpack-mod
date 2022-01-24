package net.jptrzy.inventory.backpack.integrations.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.Client;
import net.jptrzy.inventory.backpack.client.renderer.BackpackArmorRenderer;
import net.jptrzy.inventory.backpack.config.ModConfig;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;

public class BackpackTrinket implements Trinket {

    protected BackpackTrinket(){}

    public static final BackpackTrinket INSTANCE = new BackpackTrinket();

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity instanceof PlayerEntity) {
            BackpackItem.tick(stack, (PlayerEntity) entity);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return entity instanceof PlayerEntity && ModConfig.trinkets;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.world.isClient() && entity instanceof PlayerEntity
                && ((PlayerEntity) entity).currentScreenHandler instanceof PlayerScreenHandler) {
            Utils.onEquip((PlayerEntity) entity, stack);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.world.isClient() && entity instanceof PlayerEntity
                && ((PlayerEntity) entity).currentScreenHandler instanceof PlayerScreenHandler) {
            Utils.onUnEquip((PlayerEntity) entity, stack);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements TrinketRenderer {

        @Override
        public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            BipedEntityModel<LivingEntity> model = Client.BACKPACK_ARMOR_RENDERER.getModel();

            int i = ((BackpackItem) stack.getItem()).getColor(stack);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;

            model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
            model.animateModel(entity, limbAngle, limbDistance, tickDelta);

            TrinketRenderer.followBodyRotations(entity, model);

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(BackpackArmorRenderer.BACKPACK_MODEL_TEXTURE));
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1);
        }
    }
}
