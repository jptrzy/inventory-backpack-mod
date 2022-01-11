package net.jptrzy.inventory.backpack.inventory;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.mixin.SimpleInventoryAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class BackpackInventory extends SimpleInventory {

    ItemStack owner;
    int slotMove = -36;

    public BackpackInventory(ItemStack owner) {
        super(27);
        this.owner = owner;
    }

    public int moveSlot(int slot){
//        Main.LOGGER.warn(slot);
        return (slot>=0 && slot<this.size()) ? slot : slot + slotMove;
    }

    @Override
    public ItemStack getStack(int slot) {
        return super.getStack(moveSlot(slot));
    }

    @Override
    public ItemStack removeStack(int slot,  int amount) {
        return super.removeStack(moveSlot(slot), amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return super.removeStack(moveSlot(slot));
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(moveSlot(slot), stack);
    }

    @Override
    public void onOpen(PlayerEntity player){
        super.onOpen(player);
        Main.LOGGER.warn("OPEN");
        if(owner.getNbt().contains("Inventory"))
            Inventories.readNbt(owner.getNbt(), ((SimpleInventoryAccessor) this).getStacks());
//            this.readNbtList(owner.getNbt().getList("Inventory", 10));

    }

    @Override
    public void onClose(PlayerEntity player){
        super.onClose(player);
        Main.LOGGER.warn("CLOSE");
        Inventories.writeNbt(owner.getNbt(), ((SimpleInventoryAccessor) this).getStacks());
//        owner.getNbt().put("Inventory", this.toNbtList());
    }
}
