package sh.sit.utilitybelt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.sit.utilitybelt.attachment.ModAttachments;
import sh.sit.utilitybelt.component.ModComponents;
import sh.sit.utilitybelt.item.ModItems;
import sh.sit.utilitybelt.network.ModNetworking;
import sh.sit.utilitybelt.screen.ModMenuTypes;

public class UtilityBeltMod implements ModInitializer {
    public static final String MOD_ID = "utility-belt";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModAttachments.register();
        ModComponents.register();
        ModItems.register();
        ModMenuTypes.register();
        ModNetworking.register();

        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath("minecraft", "tools_and_utilities")
        )).register(output -> output.accept(ModItems.UTILITY_BELT));

        LOGGER.info("Utility Belt initialized");
    }
}
