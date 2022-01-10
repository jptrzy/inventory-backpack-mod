package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

//    @Shadow
//    @Nullable
//    private ScreenHandlerSyncHandler syncHandler;
//
//    private ItemStack cursorStack;
//    private int revision;
//
//    protected ScreenHandler getThis(){
//        return ((ScreenHandler) (Object) this);
//    }
//
//    @Inject(at = @At("HEAD"), method = "updateSlotStacks", cancellable = true)
//    private void updateSlotStacks(int revision, List<ItemStack> stacks, ItemStack cursorStack, CallbackInfo ci) {
////        Main.LOGGER.warn(getThis() instanceof InventoryScreen);
////        Main.LOGGER.warn(getThis() instanceof CraftingScreenHandler);
////        Main.LOGGER.warn(getThis() instanceof PlayerScreenHandler);
////        Main.LOGGER.warn(getThis() instanceof AbstractRecipeScreenHandler);
////        Main.LOGGER.warn(getThis() instanceof BackpackScreenHandler);
////        Main.LOGGER.warn(stacks.size());
////        Main.LOGGER.warn(getThis().slots.size());
////        Main.LOGGER.warn(syncHandler);
////        Main.LOGGER.warn(syncHandler);
////        Main.LOGGER.warn(((BackpackScreenHandler) getThis()).getCategory());
////
////        Main.LOGGER.warn(getThis().getType());
////        for(int i = 0; i < stacks.size(); ++i) {
////            if(i < getThis().slots.size()) {
////                getThis().getSlot(i).setStack((ItemStack) stacks.get(i));
////            }
////        }
////
////        cursorStack = cursorStack;
////        revision = revision;
////        ci.cancel();
//    }
}
