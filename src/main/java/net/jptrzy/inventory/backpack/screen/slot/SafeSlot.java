package net.jptrzy.inventory.backpack.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SafeSlot extends Slot {

    static final int indexMove = -36;
    final int fakeIndex;

    public SafeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index + indexMove, x, y);
        fakeIndex = index;
    }

    @Override
    public int getIndex() {
        return this.fakeIndex;
    }
}
