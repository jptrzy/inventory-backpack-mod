package net.jptrzy.inventory.backpack.old;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private PlayerEntity getThis(){
        return ((PlayerEntity) (Object) this);
    }

    @Shadow
    protected void closeHandledScreen(){}

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void tick(CallbackInfo ci) {
//        if(     !getThis().world.isClient &&
//                getThis().currentScreenHandler == getThis().playerScreenHandler &&
//                !getThis().currentScreenHandler.canUse(getThis()) &&
//                getThis().getInventory().armor.get(2).getItem() instanceof BackpackItem
//            ){
//            getThis().openHandledScreen((BackpackItem) getThis().getInventory().armor.get(2).getItem());
//        }

//        if (!this.world.isClient && this.currentScreenHandler != null && !this.currentScreenHandler.canUse(this)){
//
//        }

//        Main.LOGGER.warn(!getThis().world.isClient);
//        Main.LOGGER.warn(getThis().currentScreenHandler);
////        getThis().openHandledScreen()
//        Main.LOGGER.warn(getThis().currentScreenHandler.canUse(getThis()));
    }



}
