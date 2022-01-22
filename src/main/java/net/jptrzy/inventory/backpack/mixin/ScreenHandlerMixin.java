package net.jptrzy.inventory.backpack.mixin;

import io.netty.buffer.Unpooled;
import jdk.jshell.execution.Util;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
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

    @Inject(at = @At("HEAD"), method = "onSlotClick", cancellable = true)
    private void HEAD_onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient || slotIndex < 0 || slotIndex >= getThis().slots.size()){return;}

        if(getThis() instanceof BackpackScreenHandler) {
            ItemStack itemStack = getThis().slots.get(slotIndex).getStack();
            if (Utils.hasBackpack(player, itemStack)) {
                Utils.updateBackpackCurse(itemStack, player);
                ((BackpackScreenHandler) getThis()).saveInventory();
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "onSlotClick", cancellable = true)
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(player.world.isClient){return;}

        if(getThis() instanceof BackpackScreenHandler)
            ((BackpackScreenHandler) getThis()).dirtyBackpack = true;
    }
}
