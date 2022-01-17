package net.jptrzy.inventory.backpack.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class BackpackTrinket implements Trinket {

    public static void register() {
        TrinketsApi.registerTrinket(Main.BACKPACK, new BackpackTrinket());
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity instanceof PlayerEntity){
            BackpackItem.tick(stack, (PlayerEntity) entity);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return entity instanceof PlayerEntity && ModConfig.trinkets;
    }
}
