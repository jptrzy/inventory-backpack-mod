package net.jptrzy.inventory.backpack.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.renderer.BackpackArmorRenderer;
import net.jptrzy.inventory.backpack.client.renderer.EnderBackpackArmorRenderer;
import net.jptrzy.inventory.backpack.client.screen.BackpackScreen;
import net.jptrzy.inventory.backpack.integrations.trinkets.BackpackTrinket;
import net.jptrzy.inventory.backpack.integrations.trinkets.EnderBackpackTrinket;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {
    public static final BackpackArmorRenderer BACKPACK_ARMOR_RENDERER = new BackpackArmorRenderer();
    public static final EnderBackpackArmorRenderer ENDER_BACKPACK_ARMOR_RENDERER = new EnderBackpackArmorRenderer();

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(Main.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0? ((DyeableItem) stack.getItem()).getColor(stack) : 0xFFFFFFF, Main.BACKPACK);

        FabricModelPredicateProviderRegistry.register(Main.BACKPACK, new Identifier("locked"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return EnchantmentHelper.get(itemStack).containsKey(Enchantments.BINDING_CURSE) ? 1 : 0;
        });

        ArmorRenderer.register(BACKPACK_ARMOR_RENDERER, Main.BACKPACK);
        ArmorRenderer.register(ENDER_BACKPACK_ARMOR_RENDERER, Main.ENDER_BACKPACK);

        TrinketRendererRegistry.registerRenderer(Main.BACKPACK, new BackpackTrinket.Renderer());
        TrinketRendererRegistry.registerRenderer(Main.ENDER_BACKPACK, new EnderBackpackTrinket.Renderer());

        registerPacketHandlers();
    }

    private void registerPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(Main.NETWORK_OPEN_INVENTORY_ID,
                (client, handler, buf, sender) -> {
                    client.execute(() -> {
                        client.setScreen(new InventoryScreen(client.player));
                    });
                });
        ClientPlayNetworking.registerGlobalReceiver(Main.NETWORK_RELOAD_SCREEN_ID,
                (client, handler, buf, sender) -> {
                    client.execute(() -> {
                        if(client.currentScreen instanceof BackpackScreen)
                            ((BackpackScreen)client.currentScreen).checkColor();
                    });
                });
    }
}