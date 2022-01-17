package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

//    @Inject(at = @At("HEAD"), method = "onEquipStack", cancellable = true)
//    private void onEquipStack(ItemStack stack, CallbackInfo ci) {
//        Main.LOGGER.warn("TEST");
//    }
}
