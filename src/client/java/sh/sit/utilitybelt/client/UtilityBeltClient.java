package sh.sit.utilitybelt.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import sh.sit.utilitybelt.screen.ModMenuTypes;
import sh.sit.utilitybelt.screen.UtilityBeltScreen;

public class UtilityBeltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.UTILITY_BELT, UtilityBeltScreen::new);
        ModKeybindings.register();
    }
}
