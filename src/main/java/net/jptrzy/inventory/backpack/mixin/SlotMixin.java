package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public class SlotMixin {

    private Slot getThis(){
        return ((Slot) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "markDirty", cancellable = true)
    public void markDirty(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Main.LOGGER.warn(mc);
//        getThis().inventory
//
//        if(!(getThis() instanceof PlayerScreenHandler)){return;}
//        if(slotIndex<0 || slotIndex>=getThis().slots.size()){
//            Main.LOGGER.warn("SlotIndex out of range.");
//            return;
//        }
//        if(getThis().getSlot(slotIndex).getIndex() != 38){return;}
//
//        if(!player.world.isClient()){return;}
//
//
//
//        if(!(getThis() instanceof BackpackScreenHandler) && BackpackItem.isWearingIt(player)){
//            BackpackItem.requestBackpackMenu(true);
//        }else if(getThis() instanceof BackpackScreenHandler){
//            BackpackItem.requestBackpackMenu(false);
//            MinecraftClient.getInstance().setScreen(new InventoryScreen(player));
//        }else{
//            Main.LOGGER.warn("Unexpected situation");
//        }
    }

//    if(!(getThis() instanceof PlayerScreenHandler)){return;}
//        if(slotIndex<0 || slotIndex>=getThis().slots.size()){
//        Main.LOGGER.warn("SlotIndex out of range.");
//        return;
//    }
//        if(getThis().getSlot(slotIndex).getIndex() != 38){return;}
//
//        if(!player.world.isClient()){return;}
//
//
//
//        if(!(getThis() instanceof BackpackScreenHandler) && BackpackItem.isWearingIt(player)){
//        BackpackItem.requestBackpackMenu(true);
//    }else if(getThis() instanceof BackpackScreenHandler){
//        BackpackItem.requestBackpackMenu(false);
//        MinecraftClient.getInstance().setScreen(new InventoryScreen(player));
//    }else{
//        Main.LOGGER.warn("Unexpected situation");
//    }
}
