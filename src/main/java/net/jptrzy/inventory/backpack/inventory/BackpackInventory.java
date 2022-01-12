package net.jptrzy.inventory.backpack.inventory;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.mixin.SimpleInventoryAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class BackpackInventory extends SimpleInventory {

    ItemStack owner;
    NbtCompound tag;
    int slotMove = -36;

    public BackpackInventory(ItemStack owner) {
        super(27);
        this.owner = owner;
        this.tag = owner.getNbt();
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
//        Main.LOGGER.warn("test");
        super.setStack(moveSlot(slot), stack);
    }



    @Override
    public void onOpen(PlayerEntity player){
        super.onOpen(player);
        if(player.world.isClient()){return;}
//        if(owner.getNbt().contains("Inventory")){
        Main.LOGGER.warn(this);

        Inventories.readNbt(owner.getNbt(), ((SimpleInventoryAccessor) this).getStacks());
//            this.readNbtList(owner.getNbt().getList("Inventory", 10));

        Main.LOGGER.warn("LOAD" + this + owner.getNbt());
    }

    @Override
    public void onClose(PlayerEntity player){
        super.onClose(player);
        if(player.world.isClient()){return;}
//        Main.LOGGER.warn("CLOSE");
//        owner.getNbt().put("Inventory", this.toNbtList());

        Main.LOGGER.warn("SAVE" + this + owner.getNbt());

        Main.LOGGER.warn(player.currentScreenHandler.getCursorStack().toString());
        Main.LOGGER.warn(this.owner);
        Main.LOGGER.warn(this.tag);
        Main.LOGGER.warn(this.owner.getNbt());

        Inventories.writeNbt(owner.getNbt(), ((SimpleInventoryAccessor) this).getStacks());

    }
}
