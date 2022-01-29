package net.jptrzy.inventory.backpack.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.screen.BackpackScreen;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Unique
    private ItemStack oldItemStack = new ItemStack(Items.AIR);

    @Shadow
    @Nullable
    private ClientPlayerEntity player;

    @Shadow
    @Final
    private GameOptions options;
    @Shadow
    @Nullable
    private Screen currentScreen;

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    private void setScreen(@Nullable Screen screen, CallbackInfo ci){
        Main.LOGGER.warn(options.keyInventory.wasPressed());
        if(screen instanceof InventoryScreen && !(screen instanceof BackpackScreen) && Utils.hasBackpack(player)){
            ClientPlayNetworking.send(Main.NETWORK_BACKPACK_OPEN_ID, new PacketByteBuf(Unpooled.buffer()));
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        if(player != null && currentScreen instanceof InventoryScreen){
            ItemStack newItemStack = player.getEquippedStack(EquipmentSlot.CHEST);

            if(!oldItemStack.isItemEqual(newItemStack) && !ItemStack.areEqual(oldItemStack, newItemStack)){
                if(oldItemStack.getItem() instanceof BackpackItem){
                    Utils.onUnEquip(player, oldItemStack);
                }
                if(newItemStack.getItem() instanceof BackpackItem){
                    Utils.onEquip(player, newItemStack);
                }
            }

            oldItemStack = newItemStack.copy();
        }
    }
}
