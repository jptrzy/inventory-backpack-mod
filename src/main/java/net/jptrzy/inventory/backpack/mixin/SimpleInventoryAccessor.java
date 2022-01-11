package net.jptrzy.inventory.backpack.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleInventory.class)
public interface SimpleInventoryAccessor {

    @Accessor
    DefaultedList<ItemStack> getStacks();

}
