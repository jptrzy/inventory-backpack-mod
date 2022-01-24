package net.jptrzy.inventory.backpack;

import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.jptrzy.inventory.backpack.config.AutoConfigManager;
import net.jptrzy.inventory.backpack.integrations.trinkets.EnderBackpackTrinket;
import net.jptrzy.inventory.backpack.item.BackpackArmorMaterial;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.integrations.trinkets.BackpackTrinket;
import net.jptrzy.inventory.backpack.item.EnderBackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
	public static final String MOD_ID = "inventory_backpack";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final ArmorMaterial BACKPACK_ARMOR_MATERIAL = new BackpackArmorMaterial();

	public static final Item BACKPACK = new BackpackItem();
	public static final Item ENDER_BACKPACK = new EnderBackpackItem();

	public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER;

	public static final Identifier NETWORK_BACKPACK_OPEN_ID = id("open_backpack");
	public static final Identifier NETWORK_OPEN_INVENTORY_ID = Main.id("open_inventory");
	public static final Identifier NETWORK_RELOAD_SCREEN_ID = Main.id("reload_screen");

	@Override
	public void onInitialize() {
		if(Utils.isClothConfigLoaded()) {
			AutoConfigManager.setup();
		}

		Registry.register(Registry.ITEM, id("backpack"), BACKPACK);
		Registry.register(Registry.ITEM, id("ender_backpack"), ENDER_BACKPACK);

		if(Utils.isModLoaded(Utils.TRINKETS_MOD_ID)) {
			registerTrinkets();
		}

		registerEventsListeners();
		registerPacketHandlers();
	}

	private void registerTrinkets() {
		TrinketsApi.registerTrinket(Main.BACKPACK, BackpackTrinket.INSTANCE);
		TrinketsApi.registerTrinket(Main.ENDER_BACKPACK, EnderBackpackTrinket.INSTANCE);
	}

	private void registerEventsListeners() {
		if(Utils.isTrinketsLoaded()){
			TrinketDropCallback.EVENT.register((TrinketEnums.DropRule rule, ItemStack itemStack, SlotReference ref, LivingEntity entity)->{
				if(!(entity instanceof PlayerEntity)){return rule;}
				Utils.onBackpackDrop((PlayerEntity) entity, itemStack);
				return rule;
			});
		}
	}

	private void registerPacketHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(NETWORK_BACKPACK_OPEN_ID,
				(server, player, networkHandler, buf, sender) -> {
					server.execute(() -> {
						Utils.openBackpackHandler(Utils.hasBackpack(player), player);
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
