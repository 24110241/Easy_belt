package sh.sit.utilitybelt.component;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import sh.sit.utilitybelt.UtilityBeltMod;

public class ModComponents {
    public static final DataComponentType<BeltInventoryComponent> BELT_INVENTORY = DataComponentType.<BeltInventoryComponent>builder()
        .persistent(BeltInventoryComponent.CODEC)
        .networkSynchronized(BeltInventoryComponent.STREAM_CODEC)
        .build();

    public static void register() {
        Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(UtilityBeltMod.MOD_ID, "belt_inventory"),
            BELT_INVENTORY
        );
    }

    public static BeltInventoryComponent getBeltInventory(ItemStack stack) {
        return stack.getOrDefault(BELT_INVENTORY, BeltInventoryComponent.DEFAULT);
    }

    public static void setBeltInventory(ItemStack stack, BeltInventoryComponent component) {
        stack.set(BELT_INVENTORY, component);
    }
}
