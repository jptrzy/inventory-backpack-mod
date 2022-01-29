package net.jptrzy.inventory.backpack.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.jptrzy.inventory.backpack.config.AutoConfigManager;
import net.jptrzy.inventory.backpack.util.Utils;

public class ModMenuConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Utils.isClothConfigLoaded() ? AutoConfigManager::getConfigScreen : screen -> null;
    }
}
