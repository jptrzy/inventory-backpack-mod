package net.jptrzy.inventory.backpack.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
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
    protected ScreenHandler getThis(){
        return ((ScreenHandler) (Object) this);
    }
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


//    @Inject(at = @At("HEAD"), method = "setStackInSlot", cancellable = true)
//    public void setStackInSlot(int slot, int revision, ItemStack stack,  CallbackInfo ci) {
//        Main.LOGGER.warn(slot);
//    }

//    @Environment(EnvType.SERVER)
    @Inject(at = @At("TAIL"), method = "onSlotClick", cancellable = true)
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(!(getThis() instanceof PlayerScreenHandler)){return;}
        if(slotIndex<0 || slotIndex>=getThis().slots.size()){
            Main.LOGGER.warn("SlotIndex out of range.");
            return;
        }
        if(getThis().getSlot(slotIndex).getIndex() != 38){return;}

        if(player.world.isClient()){return;}



        if(!(getThis() instanceof BackpackScreenHandler) && BackpackItem.isWearingIt(player)){
            BackpackItem.requestBackpackMenu(true);
        }else if(getThis() instanceof BackpackScreenHandler){
            BackpackItem.requestBackpackMenu(false);
            MinecraftClient.getInstance().setScreen(new InventoryScreen(player));
        }else{
            Main.LOGGER.warn("Unexpected situation");
        }



        Main.LOGGER.warn("onSlotClick");



//        Main.LOGGER.warn(player.world.isClient());
//        Main.LOGGER.warn(slotIndex);
//        Main.LOGGER.warn(getThis().getSlot(slotIndex).getStack());
//        Main.LOGGER.warn(getThis().getSlot(slotIndex).getIndex());
//        Main.LOGGER.warn(actionType);
    }


}
