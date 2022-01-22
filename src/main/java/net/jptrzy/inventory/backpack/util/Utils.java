package net.jptrzy.inventory.backpack.util;

import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.Client;
import net.jptrzy.inventory.backpack.config.ModConfig;
import net.jptrzy.inventory.backpack.inventory.BackpackInventory;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Map;

public class Utils {

    public final static String NBT_LOCK_KEY = "Lock";

    public static final String TRINKETS_MOD_ID = "trinkets";
    public static final String CLOTH_CONFIG_MOD_ID = "cloth-config2";

    public static void setItemStackLock(ItemStack itemStack, boolean lock){
        itemStack.getOrCreateNbt().putBoolean(NBT_LOCK_KEY, lock);
    }

    public static boolean isItemStackLock(ItemStack itemStack){
        return itemStack.getOrCreateNbt().contains(NBT_LOCK_KEY) && itemStack.getNbt().getBoolean(NBT_LOCK_KEY);
    }

    public static boolean isModLoaded(String modID) {
        return FabricLoader.getInstance().isModLoaded(modID);
    }

    public static boolean isTrinketsLoaded() {
        return ModConfig.trinkets && isModLoaded(TRINKETS_MOD_ID);
    }
    public static boolean isClothConfigLoaded() {
        return isModLoaded(CLOTH_CONFIG_MOD_ID);
    }

    public static boolean hasTrinketsItem(PlayerEntity player) {
        return isTrinketsLoaded() && TrinketsApi.getTrinketComponent(player).get().isEquipped(stack -> stack.getItem() instanceof BackpackItem);
    }

    public static boolean hasBackpack(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem || hasTrinketsItem(player);
    }

    public static boolean hasBackpack(PlayerEntity player, ItemStack itemStack){
        return Utils.getBackpack(player) == itemStack;
    }

    public static ItemStack getBackpack(PlayerEntity player){
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if(itemStack.getItem() instanceof BackpackItem) {
            return itemStack;
        }

        if(isTrinketsLoaded()) {
            for(Pair<SlotReference, ItemStack> pair : TrinketsApi.getTrinketComponent(player).get().getEquipped(stack -> stack.getItem() instanceof BackpackItem)){
                itemStack = pair.getRight();
                return itemStack;
            }
        }

        return null;
    }

    public static boolean isEnderBackpack(ItemStack itemStack) {
        return itemStack.isOf(Main.ENDER_BACKPACK);
    }

    public static void updateBackpackCurse(ItemStack itemStack, PlayerEntity player){
        if(!itemStack.isOf(Main.BACKPACK)){return;}

        boolean hasItems = !((BackpackScreenHandler) player.currentScreenHandler).isEmpty();
        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(itemStack);
        boolean changedEnchants = false;
        boolean isCursed = enchants.containsKey(Enchantments.BINDING_CURSE);

        if(hasItems){
            if(!isCursed) {
                enchants.put(Enchantments.BINDING_CURSE, 1);
                changedEnchants = true;
            }
        }else{
            if(isCursed) {
                enchants.remove(Enchantments.BINDING_CURSE);
                changedEnchants = true;
            }
        }

        if(changedEnchants){
            EnchantmentHelper.set(enchants, itemStack);
        }
    }

    public static void onEquip(ClientPlayerEntity player, ItemStack stack) {
        if(Utils.hasBackpack(player, stack)){
            ClientPlayNetworking.send(Main.NETWORK_BACKPACK_OPEN_ID, new PacketByteBuf(Unpooled.buffer()));
        }
    }

    public static void onUnEquip(ClientPlayerEntity player, ItemStack stack) {
        ClientPlayNetworking.send(Main.NETWORK_BACKPACK_OPEN_ID, new PacketByteBuf(Unpooled.buffer()));
    }

    public static void openBackpackHandler(boolean open, ServerPlayerEntity player) {
        if(player.world.isClient()){
            Main.LOGGER.warn("Unauthorized use.");
            return;
        }

        ItemStack cursorStack = player.currentScreenHandler.getCursorStack();
        player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);

        player.currentScreenHandler.close(player);
        if(open){
            player.openHandledScreen((ExtendedScreenHandlerFactory) Utils.getBackpack(player).getItem());
        }else{
            player.currentScreenHandler = player.playerScreenHandler;
            ServerPlayNetworking.send(player, Client.NETWORK_OPEN_INVENTORY_ID, new PacketByteBuf(Unpooled.buffer()));
        }

        player.currentScreenHandler.setCursorStack(cursorStack);

        if(Utils.isTrinketsLoaded())
            ((TrinketPlayerScreenHandler) player.playerScreenHandler).updateTrinketSlots(false);

        player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-1, player.currentScreenHandler.nextRevision(), -1, cursorStack));
    }

    public static void onBackpackDrop(PlayerEntity player, ItemStack itemStack){
        if (!itemStack.isOf(Main.BACKPACK)) { return; }

        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(itemStack);
        boolean isCursed = enchants.containsKey(Enchantments.BINDING_CURSE);

        if (!isCursed) { return; }

        BackpackInventory inv = new BackpackInventory(itemStack);

        inv.onOpen(player);
        inv.dropAll(player);
        inv.onClose(player);

        enchants.remove(Enchantments.BINDING_CURSE);
        EnchantmentHelper.set(enchants, itemStack);

        Utils.setItemStackLock(itemStack, true);
    }
}
