package net.jptrzy.inventory.backpack.mixin;

import io.netty.buffer.Unpooled;
import jdk.jshell.execution.Util;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    private ScreenHandler getThis(){
        return ((ScreenHandler) (Object) this);
    }
    private BackpackScreenHandler getThisAsBackPack(){
        return ((BackpackScreenHandler) (Object) this);
    }

    //TODO could be a problem in the feature; may stacks could help with this
//    @Unique
//    public boolean try_open_inventory = false;
//    @Unique
//    public boolean try_open_backpack = false;
    @Unique
    public boolean hadBackpack = false;
    @Unique
    public boolean takingOfBackpack = false;

    @Inject(at = @At("HEAD"), method = "onSlotClick", cancellable = true)
    private void HEAD_onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient()){return;}
        if(slotIndex<0 || slotIndex>=getThis().slots.size()){return;}
        if(!(getThis() instanceof PlayerScreenHandler)){return;}

        hadBackpack = getThis() instanceof BackpackScreenHandler;
        if(hadBackpack) {
            ItemStack itemStack = getThis().slots.get(slotIndex).getStack();
            takingOfBackpack = Utils.hasBackpack(player, itemStack);
            if (takingOfBackpack) {
                BackpackItem.updateCurse(itemStack, player);
                ((BackpackScreenHandler) getThis()).getBackpackInventory().saveContent();
            }
        }
        Main.LOGGER.warn("HEAD {} {}", hadBackpack, takingOfBackpack);
//          OLD
//        if(player.world.isClient()){return;}
//        if(slotIndex<0 || slotIndex>=getThis().slots.size()){return;}
//        if(!(BackpackItem.isWearingIt(player, getThis().getSlot(slotIndex).getStack()) && getThis() instanceof BackpackScreenHandler )){
//            //Check if try_open_backpack for fastMove
//            if(!(getThis() instanceof BackpackScreenHandler) && getThis() instanceof PlayerScreenHandler && getThis().getSlot(slotIndex).getStack().isOf(Main.BACKPACK)){
//                try_open_backpack = true;
//                return;
//            }
//            return;
//        }
//
//        try_open_inventory = true;
//
//        BackpackItem.updateCurse(getThis().getSlot(slotIndex).getStack(), player);
//
//        ((BackpackScreenHandler) getThis()).getBackpackInventory().saveContent();
    }

    @Inject(at = @At("TAIL"), method = "onSlotClick", cancellable = true)
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient()){return;}
        if(slotIndex<0 || slotIndex>=getThis().slots.size()){return;}
        if(!(getThis() instanceof PlayerScreenHandler)){return;}

        if(hadBackpack){
            if(Utils.hasBackpack(player)){
                Main.LOGGER.warn("TEST");
                if (!Utils.hasBackpack(player, getThisAsBackPack().getBackpackInventory().getOwner())) {
                    Main.LOGGER.warn("TEST2");
                    Utils.openBackpackHandler(true, (ServerPlayerEntity) player);
                }else{
                    Main.LOGGER.warn("WTF");
                }
            }else{
                Utils.openBackpackHandler(false, (ServerPlayerEntity) player);
            }
        }else if(Utils.hasBackpack(player)){
            Utils.openBackpackHandler(true, (ServerPlayerEntity) player);
        }

        hadBackpack = false;
        takingOfBackpack = false;

        if(getThis() instanceof BackpackScreenHandler)
            ((BackpackScreenHandler) getThis()).dirtyBackpack = true;





//        Main.LOGGER.warn("TAIL {} {}", getThis().getSlot(slotIndex).getIndex(), getThis().getSlot(slotIndex).getStack());
//        ClientPlayNetworking.send(Main.id("open_backpack"), new PacketByteBuf(Unpooled.buffer()));
//          OLD
//        if(player.world.isClient()){return;}
//        if(!(getThis() instanceof PlayerScreenHandler)){return;}
//        if(slotIndex<0 || slotIndex>=getThis().slots.size()){
//            if(slotIndex == -999 && BackpackItem.isWearingIt(player) && !(getThis() instanceof BackpackScreenHandler)){
//                BackpackItem.openBackpackHandler(true, (ServerPlayerEntity) player);
//            }
//            return;
//        }
//
//        if(try_open_backpack && BackpackItem.isWearingIt(player)){
//            BackpackItem.openBackpackHandler(true, (ServerPlayerEntity) player);
//            try_open_backpack = false; // Shouldn't be necessary
//        }else {
//
//            if (try_open_inventory && !((BackpackScreenHandler) getThis()).getBackpackInventory().isEmpty()) {
//                return;
//            }
//
//            if (try_open_inventory || BackpackItem.isWearingIt(player, getThis().getSlot(slotIndex).getStack())) {
//                BackpackItem.openBackpackHandler(!try_open_inventory, (ServerPlayerEntity) player);
//                try_open_inventory = false; // Shouldn't be necessary
//            }
//
//        }
//
//        if(getThis() instanceof BackpackScreenHandler){
//            ((BackpackScreenHandler) getThis()).dirtyBackpack = true;
//        }
    }
}
