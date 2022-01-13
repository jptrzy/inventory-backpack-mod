package net.jptrzy.inventory.backpack.mixin;

import net.jptrzy.inventory.backpack.Main;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {

    @Shadow
    Map<Item, CauldronBehavior> WATER_CAULDRON_BEHAVIOR = null;
    @Shadow
    CauldronBehavior CLEAN_DYEABLE_ITEM = null;

    @Inject(at = @At("TAIL"), method = "registerBehavior", cancellable = true)
    private static void registerBehavior(CallbackInfo ci){
        WATER_CAULDRON_BEHAVIOR.put(Main.BACKPACK, CLEAN_DYEABLE_ITEM);
    }

}
