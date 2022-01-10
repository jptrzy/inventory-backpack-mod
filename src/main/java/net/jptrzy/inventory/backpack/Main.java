package net.jptrzy.inventory.backpack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
	public static final String MOD_ID = "inventory_backpack";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Item BACKPACK = new BackpackItem();

	public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER;

	@Override
	public void onInitialize() {

		Registry.register(Registry.ITEM, id("backpack"), BACKPACK);



	}

	public static Identifier id(String key){
		return new Identifier(MOD_ID, key);
	}

	static{
		BACKPACK_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(id("backpack"), BackpackScreenHandler::new);
	}
}
