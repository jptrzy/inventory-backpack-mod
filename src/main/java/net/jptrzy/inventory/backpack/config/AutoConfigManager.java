package net.jptrzy.inventory.backpack.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.example.ExampleConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.jptrzy.inventory.backpack.Main;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

public class AutoConfigManager {

    //TODO reload config on save using "ConfigSerializeEvent" somehow
    public static void setup(){
        ModConfigData data =
            AutoConfig.register(ModConfigData.class, GsonConfigSerializer::new).getConfig();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> reloadModConfig(null, data));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> reloadModConfig(null, data));

        AutoConfig.getConfigHolder(ModConfigData.class).registerLoadListener((manager, _data) -> {
            return reloadModConfig(manager, _data);
        });
        AutoConfig.getConfigHolder(ModConfigData.class).registerSaveListener((manager, _data) -> {
            return reloadModConfig(manager, _data);
        });
    }

    public static Screen getConfigScreen(Screen screen){
        return AutoConfig.getConfigScreen(ModConfigData.class, screen).get();
    }

    public static ActionResult reloadModConfig(@Nullable ConfigHolder<ModConfigData> manager, ModConfigData data){
        Main.LOGGER.info("Reload Config");

        ModConfig.trinkets = data.trinkets;

        return ActionResult.SUCCESS;
    }
}
