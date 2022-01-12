package net.jptrzy.inventory.backpack.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.inventory.BackpackInventory;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.mixin.ScreenHandlerAccessor;
import net.jptrzy.inventory.backpack.mixin.SlotAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class BackpackScreenHandler extends PlayerScreenHandler {

    private BackpackInventory backpackInventory;
    public boolean dirtyBackpack = true;

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

//        ItemStack backpack = player.getMainHandStack();
        ItemStack backpack = player.getInventory().armor.get(2);
//        backpack.setCustomName(new LiteralText("OPEN"));
//        SimpleInventory inv = ((BackpackItem) backpack.getItem()).inventory;
        backpackInventory = new BackpackInventory(backpack);
        backpackInventory.onOpen(player);
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 9; ++j) {
                int k = j + i * 9 + 36;
                addSlot(new Slot(backpackInventory, k, left + j * 18, top + i * 18));
            }
    }

    public BackpackInventory getBackpackInventory(){
        return this.backpackInventory;
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

//    @Override
//    public void setStackInSlot(int slot, int revision, ItemStack stack) {
//        Main.LOGGER.warn(slot);
//        super.setStackInSlot(slot, revision, stack);
//    }


//    @Environment(EnvType.SERVER)
//    @Override
//    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
//        super.onSlotClick(slotIndex, button, actionType, player);
//
//        Main.LOGGER.warn(slotIndex);
//        Main.LOGGER.warn(getSlot(slotIndex).getStack());
//        Main.LOGGER.warn(getSlot(slotIndex).getIndex());
//        Main.LOGGER.warn(actionType);
//
//    }

//    @Override
//    public void onContentChanged(Inventory inventory) {
//        super.onContentChanged(inventory);
//        Main.LOGGER.warn("SSSS");
//    }


}
