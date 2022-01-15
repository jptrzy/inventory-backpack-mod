package net.jptrzy.inventory.backpack.mixin;

import net.fabricmc.fabric.impl.screenhandler.client.ClientNetworking;
import net.jptrzy.inventory.backpack.Main;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworking.class)
public class ClientNetworkingMixin {

//    @Unique
//    private double x;
//    @Unique
//    private double y;
//    @Unique
//    private boolean change;
//
//    @Inject(at = @At("HEAD"), method = "openScreen", cancellable = true)
//    private void openScreenHEAD(Identifier typeId, int syncId, Text title, PacketByteBuf buf, CallbackInfo ci) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if(client.player.currentScreenHandler instanceof PlayerScreenHandler){
//            x = client.mouse.getX();
//            y = client.mouse.getY();
//            change = true;
//        }
//    }
//
//    @Inject(at = @At("TAIL"), method = "openScreen", cancellable = true)
//    private void openScreenTAIL(Identifier typeId, int syncId, Text title, PacketByteBuf buf, CallbackInfo ci) {
//        if(change){
//            Main.LOGGER.warn("{} {}", x, y);
//            MinecraftClient client = MinecraftClient.getInstance();
//            ((MouseAccessor) client.mouse).setX(x);
//            ((MouseAccessor) client.mouse).setY(y);
//        }
//        change = true;
//    }
}
