package net.jptrzy.inventory.backpack.item;

import io.netty.buffer.Unpooled;
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

    //TODO don't update curse in tick because it is not perfect. Update it in ScreenHandlerMixin.
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient) {return;}
        if (!(entity instanceof PlayerEntity)) {return;}
        if (!(((PlayerEntity) entity).currentScreenHandler instanceof BackpackScreenHandler)) {return;}
        if(!((BackpackScreenHandler) ((PlayerEntity) entity).currentScreenHandler).dirtyBackpack){return;}
        if (!Utils.hasBackpack((PlayerEntity) entity, stack)) {return;}
        ((BackpackScreenHandler) ((PlayerEntity) entity).currentScreenHandler).dirtyBackpack = false;

        BackpackItem.updateCurse(stack, (PlayerEntity) entity);
    }

    public static void updateCurse(ItemStack stack, PlayerEntity entity){
        boolean hasItems = !((BackpackScreenHandler) entity.currentScreenHandler).getBackpackInventory().isEmpty();
        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
        boolean changedEnchants = false;
        boolean isCursed = enchants.containsKey(Enchantments.BINDING_CURSE);

        if(hasItems){
            if(!isCursed) {
                enchants.put(Enchantments.BINDING_CURSE, 1);
                changedEnchants = true;
            }
        }else{
            if(isCursed) {
                enchants.remove(Enchantments.BINDING_CURSE);
                changedEnchants = true;
            }
        }

        if(changedEnchants){
            EnchantmentHelper.set(enchants, stack);
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack itemstack) {return false;}

//    @Override
//    public void onItemEntityDestroyed(ItemEntity entity) {
//        Main.LOGGER.warn("onItemEntityDestroyed");
//    }

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

    //  Statics

//    public static void lock(ItemStack itemStack, boolean lock){
//        itemStack.getOrCreateNbt().putBoolean("Lock", lock);
//    }
//
//    public static boolean isLock(ItemStack itemStack){
//        return itemStack.getOrCreateNbt().contains("Lock") && itemStack.getNbt().getBoolean("Lock");
//    }
//
//    public static boolean isWearingIt(PlayerEntity player){
//        return player.getInventory().armor.get(2).isOf(Main.BACKPACK);
//    }
//
//    public static boolean isWearingIt(PlayerEntity player, ItemStack itemStack){
//        return BackpackItem.isWearingIt(player) && player.getInventory().armor.get(2) == itemStack;
//    }
//
//    public static ItemStack getIt(PlayerEntity player){
//        return player.getInventory().armor.get(2);
//    }
//
//    //Trusts its caller and don't check if you can open Backpack Handler
//    //TODO REWRITE WHOLE FUNC
//    public static void openBackpackHandler(boolean open, ServerPlayerEntity player){
//        if(player.world.isClient()){
//            Main.LOGGER.warn("Unauthorized use.");
//            return;
//        }
//        if(player.currentScreenHandler != null){
//            ItemStack cursorStack = player.currentScreenHandler.getCursorStack();
//            player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
//
//            dropCraftingInventory(player);
//
//            //oplayer.currentScreenHandler == player.playerScreenHandler
//            if(BackpackItem.isWearingIt(player)){
//                if(open) {
//                    player.openHandledScreen((BackpackItem) BackpackItem.getIt(player).getItem());
//                }else{
//                    ServerPlayNetworking.send(player, Main.id("reload_screen"), new PacketByteBuf(Unpooled.buffer()));
//                }
//            }else if(!open && player.currentScreenHandler instanceof BackpackScreenHandler && !BackpackItem.isWearingIt(player)){
//                ServerPlayNetworking.send(player, Main.id("open_inventory"), new PacketByteBuf(Unpooled.buffer()));
//                player.currentScreenHandler.close(player);
//                player.currentScreenHandler = player.playerScreenHandler;
//            }else{
//                Main.LOGGER.warn("Unexpected situation");
//            }
//
//            player.currentScreenHandler.setCursorStack(cursorStack);
//            player.currentScreenHandler.updateToClient();
//        }else{
//            Main.LOGGER.warn("Is this even possible?");
//        }
//    }
//
//    //Literally dropInventory from ScreenHandler with CraftingInput
//    public static void dropCraftingInventory(PlayerEntity player) {
//        PlayerScreenHandler sh = ((PlayerScreenHandler) player.currentScreenHandler);
//        int i;
//        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity)player).isDisconnected()) {
//            for(i = 0; i < sh.getCraftingInput().size(); ++i) {
//                player.dropItem(sh.getCraftingInput().removeStack(i), false);
//            }
//        } else {
//            for(i = 0; i < sh.getCraftingInput().size(); ++i) {
//                PlayerInventory playerInventory = player.getInventory();
//                if (playerInventory.player instanceof ServerPlayerEntity) {
//                    playerInventory.offerOrDrop(sh.getCraftingInput().removeStack(i));
//                }
//            }
//        }
//    }
}
