package sh.sit.utilitybelt.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import sh.sit.utilitybelt.UtilityBeltMod;

public class ModItems {
    private static final Identifier BELT_ID = Identifier.fromNamespaceAndPath(UtilityBeltMod.MOD_ID, "utility_belt");

    public static final UtilityBeltItem UTILITY_BELT = new UtilityBeltItem(
        new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, BELT_ID))
            .stacksTo(1)
    );

    public static void register() {
        Registry.register(BuiltInRegistries.ITEM, BELT_ID, UTILITY_BELT);
    }
}
