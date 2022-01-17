package net.jptrzy.inventory.backpack.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
//
//    @Shadow
//    @Final
//    private PlayerEntity player;
//
//    @Inject(at = @At("TAIL"), method = "updateItems")
//    private void updateItems(CallbackInfo info) {
//        Utils.tickBackpackInTrinket(player);
//    }
}