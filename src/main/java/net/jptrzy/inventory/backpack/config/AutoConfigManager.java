package net.jptrzy.inventory.backpack.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.gui.screen.Screen;

public class AutoConfigManager {

    //TODO reload config on save using "ConfigSerializeEvent" somehow
    public static void setup(){
        ModConfigData data =
            AutoConfig.register(ModConfigData.class, GsonConfigSerializer::new).getConfig();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> reloadModConfig(data));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> reloadModConfig(data));
    }

    public static Screen getConfigScreen(Screen screen){
        return AutoConfig.getConfigScreen(ModConfigData.class, screen).get();
    }

    public static void reloadModConfig(ModConfigData data){
        ModConfig.trinkets = data.trinkets;
    }
}
