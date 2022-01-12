package net.jptrzy.inventory.backpack.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.screen.BackpackScreen;
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
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

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
