package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private PlayerEntity getThis(){
        return ((PlayerEntity) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", cancellable = true)
    private void dropItem(ItemStack itemStack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        Utils.onBackpackDrop(getThis(), itemStack);
    }

    @Inject(at = @At("HEAD"), method = "dropInventory", cancellable = true)
    private void dropInventory(CallbackInfo ci) {
        if(!(getThis().currentScreenHandler instanceof BackpackScreenHandler && Utils.hasBackpack(getThis()))){ return; }

        ((BackpackScreenHandler) getThis().currentScreenHandler).getBackpackInventory().saveContent();
    }
}
