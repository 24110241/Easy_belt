package sh.sit.utilitybelt.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import sh.sit.utilitybelt.attachment.ModAttachments;
import sh.sit.utilitybelt.screen.UtilityBeltScreenHandler;

public class UtilityBeltItem extends Item {
    public static final int ATTACHMENT_SLOT = -1;

    public UtilityBeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        ItemStack equippedBelt = ModAttachments.getBelt(player);

        if (!level.isClientSide()) {
            if (!equippedBelt.isEmpty()) {
                if (heldStack.getItem() instanceof UtilityBeltItem && heldStack != equippedBelt) {
                    swapBelt(player, hand, heldStack, equippedBelt);
                }
                player.openMenu(new UtilityBeltScreenHandler.Provider(
                    ModAttachments.getBelt(player), ATTACHMENT_SLOT
                ));
            } else {
                equipBelt(player, hand, heldStack);
                player.openMenu(new UtilityBeltScreenHandler.Provider(
                    ModAttachments.getBelt(player), ATTACHMENT_SLOT
                ));
            }
        }

        return InteractionResult.SUCCESS;
    }

    private static void equipBelt(Player player, InteractionHand hand, ItemStack heldStack) {
        ItemStack toEquip = heldStack.copy();
        toEquip.setCount(1);
        ModAttachments.setBelt(player, toEquip);
        heldStack.shrink(1);
    }

    private static void swapBelt(Player player, InteractionHand hand, ItemStack heldStack, ItemStack equippedBelt) {
        ModAttachments.setBelt(player, heldStack.copy());
        player.setItemInHand(hand, equippedBelt.copy());
    }

    public static boolean isValidWeaponSlot(ItemStack stack) {
        if (stack.isEmpty()) return true;
        Item item = stack.getItem();
        return item instanceof net.minecraft.world.item.BowItem
            || item instanceof net.minecraft.world.item.CrossbowItem
            || item instanceof net.minecraft.world.item.TridentItem
            || item instanceof net.minecraft.world.item.ShovelItem
            || item instanceof net.minecraft.world.item.AxeItem
            || item instanceof net.minecraft.world.item.HoeItem
            || item instanceof net.minecraft.world.item.MaceItem;
    }

    public static boolean isValidStorageSlot(ItemStack stack) {
        if (stack.isEmpty()) return true;
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof ShulkerBoxBlock) return false;
        }
        return !stack.has(net.minecraft.core.component.DataComponents.CONTAINER);
    }
}
