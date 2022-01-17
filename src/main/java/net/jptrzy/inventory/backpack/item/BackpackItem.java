package net.jptrzy.inventory.backpack.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import io.netty.buffer.Unpooled;
import jdk.jshell.execution.Util;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BackpackItem extends DyeableArmorItem implements ExtendedScreenHandlerFactory {

    private static final Text TITLE = new TranslatableText("container." + Main.MOD_ID + ".backpack");

    public BackpackItem() {
        super(
                ArmorMaterials.LEATHER,
                EquipmentSlot.CHEST,
                new Item.Settings().group(ItemGroup.COMBAT).maxDamage(-1)
        );
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!(entity instanceof PlayerEntity)) {return;}
        tick(stack, (PlayerEntity) entity);
    }

    //TODO don't update curse in tick because it is not perfect. Update it in ScreenHandlerMixin.
    public static void tick(ItemStack stack, PlayerEntity entity) {
        if (entity.world.isClient) {return;}
        if (!(entity.currentScreenHandler instanceof BackpackScreenHandler)) {return;}
        if(!((BackpackScreenHandler) entity.currentScreenHandler).dirtyBackpack){return;}
        if (!Utils.hasBackpack(entity, stack)) {return;}
        ((BackpackScreenHandler) entity.currentScreenHandler).dirtyBackpack = false;

        Utils.updateBackpackCurse(stack, entity);
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack itemstack) {return false;}

    //Screen
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BackpackScreenHandler(syncId, player);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
    }

    @Override
    public Text getDisplayName() {
        return null;
    }
}
