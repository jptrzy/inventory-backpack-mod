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
        if((slotIndex<0 || slotIndex>=getThis().slots.size()) && slotIndex != -999){return;}
        if(!(getThis() instanceof PlayerScreenHandler)){return;}

        hadBackpack = getThis() instanceof BackpackScreenHandler;
        if(hadBackpack && slotIndex != -999) {
            ItemStack itemStack = getThis().slots.get(slotIndex).getStack();
            takingOfBackpack = Utils.hasBackpack(player, itemStack);
            if (takingOfBackpack) {
                Utils.updateBackpackCurse(itemStack, player);
                ((BackpackScreenHandler) getThis()).getBackpackInventory().saveContent();
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "onSlotClick", cancellable = true)
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient()){return;}
        if((slotIndex<0 || slotIndex>=getThis().slots.size()) && slotIndex != -999){return;}
        if(!(getThis() instanceof PlayerScreenHandler)){return;}

        if(hadBackpack){
            if(Utils.hasBackpack(player)){
                if (!Utils.hasBackpack(player, getThisAsBackPack().getBackpackInventory().getOwner())) {
                    Utils.openBackpackHandler(true, (ServerPlayerEntity) player);
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
    }
}
