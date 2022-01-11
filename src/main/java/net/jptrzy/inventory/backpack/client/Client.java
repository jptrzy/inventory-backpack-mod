package net.jptrzy.inventory.backpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.screen.BackpackScreen;
import net.minecraft.item.DyeableItem;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(Main.BACKPACK_SCREEN_HANDLER, BackpackScreen::new);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0? ((DyeableItem) stack.getItem()).getColor(stack) : 0xFFFFFFF, Main.BACKPACK);
    }
}