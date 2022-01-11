package net.jptrzy.inventory.backpack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

		registerPacketHandlers();

	}

	private void registerPacketHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(Main.id("open_backpack"),
				(server, player, networkHandler, buf, sender) -> {
					boolean open = buf.readNbt().getBoolean("Open");
					server.execute(() -> {
						Main.LOGGER.warn("get");
						if(player.currentScreenHandler != null){
							ItemStack cursorStack = player.currentScreenHandler.getCursorStack();
							player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);

							Main.LOGGER.warn(open);
							Main.LOGGER.warn(player.currentScreenHandler);
							Main.LOGGER.warn(BackpackItem.isWearingIt(player));

							if(open && player.currentScreenHandler == player.playerScreenHandler && BackpackItem.isWearingIt(player)){
								player.openHandledScreen((BackpackItem) BackpackItem.getIt(player).getItem());
							}else if(player.currentScreenHandler instanceof BackpackScreenHandler){
//								player.closeHandledScreen();
								player.currentScreenHandler = player.playerScreenHandler;
							}else{
								Main.LOGGER.warn("Unexpected situation");
							}

							player.currentScreenHandler.setCursorStack(cursorStack);
							player.currentScreenHandler.updateToClient();
						}
					});
				});
	}

	public static Identifier id(String key){
		return new Identifier(MOD_ID, key);
	}

	static{
		BACKPACK_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(id("backpack"), BackpackScreenHandler::new);
	}
}
