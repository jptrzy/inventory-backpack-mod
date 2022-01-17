package net.jptrzy.inventory.backpack;

import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.jptrzy.inventory.backpack.config.AutoConfigManager;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.integrations.BackpackTrinket;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
	public static final String MOD_ID = "inventory_backpack";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


	public static final Identifier BACKPACK_ID = id("backpack");
	public static final Item BACKPACK = new BackpackItem();

	public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER;

	@Override
	public void onInitialize() {

		if(Utils.isClothConfigLoaded()){
			AutoConfigManager.setup();
		}

		if(Utils.isModLoaded(Utils.TRINKETS_MOD_ID)){
			BackpackTrinket.register();
		}

		Registry.register(Registry.ITEM, BACKPACK_ID, BACKPACK);

		registerEventsListiners();
		registerPacketHandlers();
	}

	private void registerEventsListiners() {
		if(Utils.isTrinketsLoaded()){
			TrinketDropCallback.EVENT.register((TrinketEnums.DropRule rule, ItemStack itemStack, SlotReference ref, LivingEntity entity)->{
				if(!(entity instanceof PlayerEntity)){return rule;}
				Utils.onBackpackDrop((PlayerEntity) entity, itemStack);
				return rule;
			});
		}
	}

	private void registerPacketHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(Main.id("open_backpack"),
				(server, player, networkHandler, buf, sender) -> {
					server.execute(() -> {
						if(player.currentScreenHandler == player.playerScreenHandler && Utils.hasBackpack(player)){
							Utils.openBackpackHandler(true, player);
						}else{
							ServerPlayNetworking.send(player, Main.id("open_inventory"), new PacketByteBuf(Unpooled.buffer()));
							Main.LOGGER.warn("This shouldn't be possible.");
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
