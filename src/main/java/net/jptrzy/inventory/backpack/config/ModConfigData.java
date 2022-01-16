package net.jptrzy.inventory.backpack.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.jptrzy.inventory.backpack.Main;

@Config(name = Main.MOD_ID)
public class ModConfigData implements ConfigData {
    @ConfigEntry.Category("Server")
    public boolean trinkets = true;
}
