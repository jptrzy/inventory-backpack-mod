package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    private ServerPlayerEntity getThis(){
        return ((ServerPlayerEntity) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "onDisconnect", cancellable = true)
    private void onDisconnect(CallbackInfo ci) {
        if(!(getThis().currentScreenHandler instanceof BackpackScreenHandler && Utils.hasBackpack(getThis()))){ return; }
        ((BackpackScreenHandler) getThis().currentScreenHandler).saveInventory();
    }
}
