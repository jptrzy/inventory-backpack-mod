package net.jptrzy.inventory.backpack.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.jptrzy.inventory.backpack.config.AutoConfigManager;
import net.jptrzy.inventory.backpack.config.ModConfigData;
import net.jptrzy.inventory.backpack.util.Utils;

public class ModMenuConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Utils.isClothConfigLoaded() ? AutoConfigManager::getConfigScreen : null;
    }
}
