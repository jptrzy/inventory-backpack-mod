package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.inventory.BackpackInventory;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.Nullable;
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
    private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (!stack.isOf(Main.BACKPACK)) { return; }

        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
        boolean isCursed = enchants.containsKey(Enchantments.BINDING_CURSE);

        if (!isCursed) { return; }

        BackpackInventory inv = new BackpackInventory(stack);

        inv.onOpen(getThis());
        inv.dropAll(getThis());
        inv.onClose(getThis());

        enchants.remove(Enchantments.BINDING_CURSE);
        EnchantmentHelper.set(enchants, stack);

        BackpackItem.lock(stack, true);
    }

    @Inject(at = @At("HEAD"), method = "dropInventory", cancellable = true)
    private void dropInventory(CallbackInfo ci) {
        if(!(getThis().currentScreenHandler instanceof BackpackScreenHandler && BackpackItem.isWearingIt(getThis()))){ return; }

        ((BackpackScreenHandler) getThis().currentScreenHandler).getBackpackInventory().saveContent();
    }
}
