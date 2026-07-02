package sh.sit.utilitybelt.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import sh.sit.utilitybelt.attachment.ModAttachments;
import sh.sit.utilitybelt.component.BeltInventoryComponent;
import sh.sit.utilitybelt.component.ModComponents;
import sh.sit.utilitybelt.item.UtilityBeltItem;

public class UtilityBeltScreenHandler extends AbstractContainerMenu {
    private static final int ATTACHMENT_SLOT = -1;

    private final Inventory playerInventory;
    private ItemStack beltStack;
    private final int heldSlot;
    private BeltInventoryComponent.Mutable beltContainer;

    // Client-side constructor (MenuType factory)
    public UtilityBeltScreenHandler(int syncId, Inventory playerInventory, int heldSlot) {
        super(ModMenuTypes.UTILITY_BELT, syncId);
        this.playerInventory = playerInventory;
        this.heldSlot = heldSlot;
        this.beltStack = resolveBeltStack(playerInventory, heldSlot);
        this.beltContainer = new BeltInventoryComponent.Mutable(ModComponents.getBeltInventory(this.beltStack));
        init();
    }

    // Server-side constructor with exact belt stack
    private UtilityBeltScreenHandler(int syncId, Inventory playerInventory, int heldSlot, ItemStack beltStack) {
        super(ModMenuTypes.UTILITY_BELT, syncId);
        this.playerInventory = playerInventory;
        this.heldSlot = heldSlot;
        this.beltStack = beltStack;
        this.beltContainer = new BeltInventoryComponent.Mutable(ModComponents.getBeltInventory(beltStack));
        init();
    }

    public static UtilityBeltScreenHandler createServer(int syncId, Inventory playerInventory, ItemStack beltStack, int heldSlot) {
        return new UtilityBeltScreenHandler(syncId, playerInventory, heldSlot, beltStack);
    }

    private static ItemStack resolveBeltStack(Inventory inventory, int heldSlot) {
        if (heldSlot == ATTACHMENT_SLOT) {
            return ModAttachments.getBelt(inventory.player);
        }
        if (heldSlot >= 0 && heldSlot < 9) {
            ItemStack stack = inventory.getItem(heldSlot);
            if (stack.getItem() instanceof UtilityBeltItem) return stack;
        }
        if (heldSlot == 40) {
            ItemStack stack = inventory.getItem(40);
            if (stack.getItem() instanceof UtilityBeltItem) return stack;
        }
        return ItemStack.EMPTY;
    }

    private void init() {
        addBeltSlots();
        addPlayerInventory();
        addPlayerHotbar();
    }

    private void addBeltSlots() {
        for (int i = 0; i < BeltInventoryComponent.BELT_SIZE; i++) {
            addSlot(new BeltSlot(beltContainer, i, getSlotX(i), getSlotY(i)));
        }
    }

    private int getSlotX(int index) {
        if (index < 2) return 62 + index * 54;
        return 8 + (index - 2) % 9 * 18;
    }

    private int getSlotY(int index) {
        if (index < 2) return 8;
        return 30 + (index - 2) / 9 * 18;
    }

    private void addPlayerInventory() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar() {
        for (int col = 0; col < 9; col++) {
            if (col == heldSlot) {
                addSlot(new LockedSlot(playerInventory, col, 8 + col * 18, 142));
            } else {
                addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            result = slotStack.copy();

            int beltSlotCount = BeltInventoryComponent.BELT_SIZE;
            int playerStart = beltSlotCount;
            int playerEnd = beltSlotCount + 36;

            if (index < beltSlotCount) {
                if (!moveItemStackTo(slotStack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                boolean moved = false;
                if (UtilityBeltItem.isValidWeaponSlot(slotStack)) {
                    moved = moveItemStackTo(slotStack, 0, 2, false);
                }
                if (!moved && UtilityBeltItem.isValidStorageSlot(slotStack)) {
                    moved = moveItemStackTo(slotStack, 2, beltSlotCount, false);
                }
                if (!moved) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        if (heldSlot == ATTACHMENT_SLOT) {
            return ModAttachments.hasBelt(player);
        }
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof UtilityBeltItem) return true;
        ItemStack offHand = player.getOffhandItem();
        return offHand.getItem() instanceof UtilityBeltItem;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        saveInventory();
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (!playerInventory.player.level().isClientSide()) {
            saveInventory();
        }
    }

    private void saveInventory() {
        if (beltStack.isEmpty()) return;
        BeltInventoryComponent saved = beltContainer.toImmutable();
        ModComponents.setBeltInventory(beltStack, saved);
        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            if (heldSlot == ATTACHMENT_SLOT) {
                ModAttachments.setBelt(serverPlayer, beltStack);
            } else if (heldSlot >= 0 && heldSlot < 9) {
                serverPlayer.getInventory().setItem(heldSlot, beltStack);
            } else if (heldSlot == 40) {
                serverPlayer.getInventory().setItem(40, beltStack);
            }
        }
    }

    public int getBeltInventorySize() {
        return beltContainer.getContainerSize();
    }

    public int getBeltRows() {
        return (int) Math.ceil((float) getBeltInventorySize() / 9);
    }

    public record Provider(ItemStack beltStack, int heldSlot) implements MenuProvider {
        @Override
        public Component getDisplayName() {
            return Component.translatable("container.utility-belt");
        }

        @Override
        public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
            return UtilityBeltScreenHandler.createServer(syncId, inventory, beltStack, heldSlot);
        }
    }

    private static class BeltSlot extends Slot {
        public BeltSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (getContainerSlot() < 2) return UtilityBeltItem.isValidWeaponSlot(stack);
            return UtilityBeltItem.isValidStorageSlot(stack);
        }
    }

    private static class LockedSlot extends Slot {
        public LockedSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
