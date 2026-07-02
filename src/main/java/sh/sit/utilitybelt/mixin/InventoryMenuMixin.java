package sh.sit.utilitybelt.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.sit.utilitybelt.attachment.ModAttachments;
import sh.sit.utilitybelt.item.UtilityBeltItem;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin {
    @Unique
    private static final int BELT_SLOT_INDEX = 46;

    @Unique
    private Player playerRef;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addBeltSlot(net.minecraft.world.entity.player.Inventory inventory, boolean active, Player player, CallbackInfo ci) {
        this.playerRef = player;
        BeltContainer beltContainer = new BeltContainer(player);
        ((AbstractContainerMenuAccessor) this).invokeAddSlot(new Slot(beltContainer, 0, 77, 44) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof UtilityBeltItem;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public Identifier getNoItemIcon() {
                return Identifier.fromNamespaceAndPath("utility-belt", "gui/utility_belt");
            }

            @Override
            public boolean isActive() {
                return playerRef.containerMenu instanceof InventoryMenu;
            }
        });
    }

    @Unique
    private static class BeltContainer implements Container {
        private final Player player;

        BeltContainer(Player player) {
            this.player = player;
        }

        @Override
        public ItemStack getItem(int slot) {
            return ModAttachments.getBelt(player);
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            ModAttachments.setBelt(player, stack);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack stack = getItem(slot);
            if (stack.isEmpty()) return ItemStack.EMPTY;
            if (amount >= stack.getCount()) {
                setItem(slot, ItemStack.EMPTY);
                return stack;
            }
            ItemStack split = stack.split(amount);
            setItem(slot, stack);
            return split;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack stack = getItem(slot);
            setItem(slot, ItemStack.EMPTY);
            return stack;
        }

        @Override
        public void setChanged() {}

        @Override
        public boolean stillValid(net.minecraft.world.entity.player.Player player) {
            return true;
        }

        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return getItem(0).isEmpty();
        }

        @Override
        public void clearContent() {
            setItem(0, ItemStack.EMPTY);
        }
    }
}
