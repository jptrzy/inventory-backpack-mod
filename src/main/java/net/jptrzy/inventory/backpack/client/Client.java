package net.jptrzy.inventory.backpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.renderer.BackpackArmorRenderer;
import net.jptrzy.inventory.backpack.client.screen.BackpackScreen;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    public static final Identifier NETWORK_OPEN_INVENTORY_ID = Main.id("open_inventory");
    public static final Identifier NETWORK_RELOAD_SCREEN_ID = Main.id("reload_screen");

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

        ArmorRenderer.register(new BackpackArmorRenderer(), Main.BACKPACK);

        registerPacketHandlers();
    }

    private void registerPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(NETWORK_OPEN_INVENTORY_ID,
                (client, handler, buf, sender) -> {
                    client.execute(() -> {
                        client.setScreen(new InventoryScreen(client.player));
                    });
                });
        ClientPlayNetworking.registerGlobalReceiver(NETWORK_RELOAD_SCREEN_ID,
                (client, handler, buf, sender) -> {
                    client.execute(() -> {
                        if(client.currentScreen instanceof BackpackScreen)
                            ((BackpackScreen)client.currentScreen).checkColor();
                    });
                });
    }
}