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
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    private void setScreen(@Nullable Screen screen, CallbackInfo ci){
        if(screen instanceof InventoryScreen && !(screen instanceof BackpackScreen) && Utils.hasBackpack(player)){
            ClientPlayNetworking.send(Main.NETWORK_BACKPACK_OPEN_ID, new PacketByteBuf(Unpooled.buffer()));
            ci.cancel();
        }
    }
}
