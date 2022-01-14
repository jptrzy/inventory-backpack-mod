package net.jptrzy.inventory.backpack.util;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Utils {

    public final static String NBT_LOCK_KEY = "Lock";

    public static final String TRINKETS_MOD_ID = "trinkets";

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
        return isModLoaded(TRINKETS_MOD_ID);
    }

    public static boolean hasTrinketsItem(PlayerEntity player, Item item) {
        return isTrinketsLoaded() && TrinketsApi.getTrinketComponent(player).get().isEquipped(item);
    }

    public static boolean hasTrinketsItem(PlayerEntity player, Item item, ItemStack itemStack) {
        return hasTrinketsItem(player, item) && TrinketsApi.getTrinketComponent(player).get().getEquipped(item).contains(itemStack);
    }

    public static boolean hasBackpack(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.CHEST).isOf(Main.BACKPACK) || hasTrinketsItem(player, Main.BACKPACK);
    }

    public static boolean hasBackpack(PlayerEntity player, ItemStack itemStack){
        return hasBackpack(player) && (player.getEquippedStack(EquipmentSlot.CHEST) == itemStack || hasTrinketsItem(player, Main.BACKPACK, itemStack));
    }

    public static ItemStack getBackpack(PlayerEntity player){
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if(hasBackpack(player, itemStack)) {
            return itemStack;
        }

        if(isTrinketsLoaded()) {
            itemStack = TrinketsApi.getTrinketComponent(player).get().getEquipped(Main.BACKPACK).get(0).getRight();
            if (hasBackpack(player, itemStack))
                return itemStack;
        }
        return ItemStack.EMPTY;
    }

}
