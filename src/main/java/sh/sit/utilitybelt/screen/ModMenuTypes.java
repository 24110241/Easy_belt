package sh.sit.utilitybelt.screen;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.flag.FeatureFlagSet;
import sh.sit.utilitybelt.UtilityBeltMod;

public class ModMenuTypes {
    public static final MenuType<UtilityBeltScreenHandler> UTILITY_BELT = new MenuType<>(
        (syncId, inventory) -> new UtilityBeltScreenHandler(syncId, inventory, -1),
        FeatureFlagSet.of()
    );

    public static void register() {
        Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath(UtilityBeltMod.MOD_ID, "utility_belt"),
            UTILITY_BELT
        );
    }
}
