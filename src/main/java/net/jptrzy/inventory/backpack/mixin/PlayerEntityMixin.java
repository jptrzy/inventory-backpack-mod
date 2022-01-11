package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private PlayerEntity getThis(){
        return ((PlayerEntity) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "tick", cancellable = true)
    private void tick(CallbackInfo ci) {
        if(!getThis().world.isClient && getThis().currentScreenHandler == getThis().playerScreenHandler && !getThis().currentScreenHandler.canUse(getThis()) && getThis().getInventory().armor.get(2).getItem() instanceof BackpackItem){
            getThis().openHandledScreen((BackpackItem) getThis().getInventory().armor.get(2).getItem());
        }
    }

}
