package sh.sit.utilitybelt.attachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sh.sit.utilitybelt.UtilityBeltMod;

public class ModAttachments {
    public static final AttachmentType<ItemStack> BELT_SLOT = AttachmentRegistry.<ItemStack>builder()
        .persistent(ItemStack.CODEC)
        .syncWith(ItemStack.OPTIONAL_STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
        .initializer(() -> ItemStack.EMPTY)
        .buildAndRegister(Identifier.fromNamespaceAndPath(UtilityBeltMod.MOD_ID, "belt_slot"));

    public static void register() {
    }

    public static ItemStack getBelt(Player player) {
        return player.getAttachedOrElse(BELT_SLOT, ItemStack.EMPTY);
    }

    public static void setBelt(Player player, ItemStack stack) {
        player.setAttached(BELT_SLOT, stack);
    }

    public static boolean hasBelt(Player player) {
        return player.hasAttached(BELT_SLOT) && !getBelt(player).isEmpty();
    }
}
