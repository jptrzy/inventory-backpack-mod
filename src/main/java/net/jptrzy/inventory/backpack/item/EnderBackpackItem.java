package net.jptrzy.inventory.backpack.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnderBackpackItem extends BackpackItem {

    public EnderBackpackItem(){
        super();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {}
}
