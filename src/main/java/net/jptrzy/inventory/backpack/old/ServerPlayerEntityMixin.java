package net.jptrzy.inventory.backpack.old;

import net.jptrzy.inventory.backpack.Main;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
//    @Inject(at = @At("HEAD"), method = "openHandledScreen", cancellable = true)
//    private void openHandledScreen(@Nullable NamedScreenHandlerFactory factory, CallbackInfoReturnable<OptionalInt> cir) {
//        Main.LOGGER.warn("WORK");
//    }
//
//    @Inject(at = @At("HEAD"), method = "onScreenHandlerOpened", cancellable = true)
//    private void onScreenHandlerOpened(ScreenHandler screenHandler, CallbackInfo ci) {
//        Main.LOGGER.warn("WORK 22");
//    }
}
