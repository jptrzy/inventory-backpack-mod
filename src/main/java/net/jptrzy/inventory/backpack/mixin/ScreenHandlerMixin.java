package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    protected ScreenHandler getThis(){
        return ((ScreenHandler) (Object) this);
    }

    //It works!!!
//    @Unique
    public boolean open_inventory = false;

    //Save Backpack Inventory
    //TODO if problem with backpack curse, then you can update it here.
    //TODO | or just check on pickup if not empty inventory and fast apply curse
    @Inject(at = @At("HEAD"), method = "onSlotClick", cancellable = true)
    private void HEAD_onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient()){return;}
//        if(slotIndex == -999){return;}
        if(slotIndex<0 || slotIndex>=getThis().slots.size()){return;}
        if(!(BackpackItem.isWearingIt(player, getThis().getSlot(slotIndex).getStack()))){return;}

        if(!(getThis() instanceof BackpackScreenHandler)){return;}
        open_inventory = true;


        BackpackItem.updateCurse(getThis().getSlot(slotIndex).getStack(), player);

        ((BackpackScreenHandler) getThis()).getBackpackInventory().saveContent();
    }

    @Inject(at = @At("TAIL"), method = "onSlotClick", cancellable = true)
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient()){return;}
        if(!(getThis() instanceof PlayerScreenHandler)){return;}
        if(slotIndex<0 || slotIndex>=getThis().slots.size()){
            if(slotIndex == -999 && BackpackItem.isWearingIt(player) && !(getThis() instanceof BackpackScreenHandler)){
                BackpackItem.openBackpackHandler(true, (ServerPlayerEntity) player);
            }
            return;
        }

        if(open_inventory && !((BackpackScreenHandler)getThis()).getBackpackInventory().isEmpty()){
            return;
        }

        if(open_inventory || BackpackItem.isWearingIt(player, getThis().getSlot(slotIndex).getStack())){
            BackpackItem.openBackpackHandler(!open_inventory, (ServerPlayerEntity) player);
            open_inventory = false; // Shouldn't be necessary
        }

        if(getThis() instanceof BackpackScreenHandler){
            ((BackpackScreenHandler) getThis()).dirtyBackpack = true;
        }
    }
}
