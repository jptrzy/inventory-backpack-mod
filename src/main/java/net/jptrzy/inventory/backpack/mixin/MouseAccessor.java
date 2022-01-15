package net.jptrzy.inventory.backpack.mixin;

import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mouse.class)
public interface MouseAccessor {
    @Accessor("x")
    void setX(double x);

    @Accessor("y")
    void setY(double y);
}
