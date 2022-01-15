package net.jptrzy.inventory.backpack.screen;

import dev.emi.trinkets.TrinketPlayerScreenHandler;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.inventory.BackpackInventory;
import net.jptrzy.inventory.backpack.mixin.ScreenHandlerAccessor;
import net.jptrzy.inventory.backpack.mixin.SlotAccessor;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class BackpackScreenHandler extends PlayerScreenHandler {

    private BackpackInventory backpackInventory;
    public boolean dirtyBackpack = true;
    private final int backpackStart;

    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory.player);
    }

    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(syncId, playerInventory);
    }

    public BackpackScreenHandler(int syncId, PlayerEntity player) {
        super(player.getInventory(), !player.world.isClient(), player);


        // Without this ScreenHandler thinks that it is PlayerScreenHandler and not BackpackScreenHandler from the client site. (I've worked on it for too long.)
        ((ScreenHandlerAccessor) this).setSyncId(syncId);

        this.enableSyncing();

        Inventory inventory = player.getInventory();
        for(Slot slot : slots){
            if (slot.inventory == inventory && slot.getIndex() < inventory.size() - 5) {
                ((SlotAccessor) slot).setY(((SlotAccessor) slot).getY() + 58);
            }
        }

        Slot anchor = slots.get(9);
        int left = anchor.x;
        int top = anchor.y - 58;

        backpackStart = slots.size();

        ItemStack backpack = Utils.getBackpack(player);
        backpackInventory = new BackpackInventory(backpack);
        backpackInventory.onOpen(player);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 9; ++j) {
                int k = j + i * 9 + 36;
                addSlot(new Slot(backpackInventory, k, left + j * 18, top + i * 18));
            }

//        if(Utils.isTrinketsLoaded())
//            ((TrinketPlayerScreenHandler) player.playerScreenHandler).updateTrinketSlots(true);
//        Main.LOGGER.warn(slots.size());
    }

    public BackpackInventory getBackpackInventory(){
        return this.backpackInventory;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
//        final int craftingResult = 0;
//        final int craftingStart = 1;
//        final int armorStart = 5;
        final int inventoryStart = 9;
        final int hotbarStart = 36;
        final int offHand = 45;
        final int backpackEnd = backpackStart+26;

        Main.LOGGER.warn("{} {} {}", backpackStart, backpackEnd, slots.size());



        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
            if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !((Slot)this.slots.get(8 - equipmentSlot.getEntitySlotId())).hasStack()) {
                int i = 8 - equipmentSlot.getEntitySlotId();
                if (!this.insertItem(itemStack2, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentSlot == EquipmentSlot.OFFHAND && !((Slot)this.slots.get(offHand)).hasStack()) {
                if (!this.insertItem(itemStack2, offHand, backpackStart, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= inventoryStart && index < hotbarStart) {
                if (!this.insertItem(itemStack2, backpackStart, backpackEnd+1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= backpackStart && index < backpackEnd+1) {
                if (!this.insertItem(itemStack2, inventoryStart, hotbarStart, false)) {
                    return ItemStack.EMPTY;
                }
            }else{
                return super.transferSlot(player, index);
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        backpackInventory.onClose(player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    //alternatively in constructor '((ScreenHandlerAccessor) this).setType(Main.BACKPACK_SCREEN_HANDLER);'
    @Override
    public ScreenHandlerType<?> getType() {
        return Main.BACKPACK_SCREEN_HANDLER;
    }
}
