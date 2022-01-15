package net.jptrzy.inventory.backpack.inventory;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.mixin.SimpleInventoryAccessor;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.List;

public class BackpackInventory extends SimpleInventory {

    ItemStack owner;
    int slotMove = -36;

    public BackpackInventory(ItemStack owner) {
        super(27);
        this.owner = owner;
    }

    public int moveSlot(int slot){
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
    public void setStack(int slot, ItemStack stack) { super.setStack(moveSlot(slot), stack); }

    @Override
    public void onOpen(PlayerEntity player){
        super.onOpen(player);
        if(player.world.isClient()){return;}
        if(!owner.isOf(Main.BACKPACK)){return;}
        if(!owner.hasNbt()){return;}

        Utils.setItemStackLock(owner, false);

        Inventories.readNbt(owner.getNbt(), ((SimpleInventoryAccessor) this).getStacks());
    }

    //TODO After death its save after some time you could yous it
    public void saveContent(){
        if(!Utils.isItemStackLock(owner)){
            Inventories.writeNbt(owner.getNbt(), ((SimpleInventoryAccessor) this).getStacks());
        }else{
            //TODO After death its save after some time (and after pressing restart`    ) you could yous it
//            Main.LOGGER.warn("TRY SAVING {}", this);
        }
    }

    @Override
    public void onClose(PlayerEntity player){
        super.onClose(player);
        if(player.world.isClient()){return;}
        if(!owner.isOf(Main.BACKPACK)){return;}

        this.saveContent();
    }

    public void dropAll(PlayerEntity player) {
        for(int i = 0; i < size(); ++i) {
            ItemStack itemStack = (ItemStack)getStack(i);
            if (!itemStack.isEmpty()) {
                player.dropItem(itemStack, true, false);
                setStack(i, ItemStack.EMPTY);
            }
        }
    }
}
