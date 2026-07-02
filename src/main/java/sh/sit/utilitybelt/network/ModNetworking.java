package sh.sit.utilitybelt.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import sh.sit.utilitybelt.attachment.ModAttachments;
import sh.sit.utilitybelt.component.BeltInventoryComponent;
import sh.sit.utilitybelt.component.ModComponents;
import sh.sit.utilitybelt.item.UtilityBeltItem;

public class ModNetworking {
    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(QuickDrawPayload.TYPE, QuickDrawPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(QuickDrawPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();
                int beltSlot = payload.slot();

                if (beltSlot < 0 || beltSlot > 1) return;

                ItemStack handItem = player.getMainHandItem();

                ItemStack beltStack;
                boolean beltInHand = handItem.getItem() instanceof UtilityBeltItem;
                boolean beltFromAttachment = false;

                if (beltInHand) {
                    beltStack = handItem;
                } else {
                    beltStack = ModAttachments.getBelt(player);
                    if (beltStack.isEmpty()) {
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = player.getInventory().getItem(i);
                            if (stack.getItem() instanceof UtilityBeltItem) {
                                beltStack = stack;
                                break;
                            }
                        }
                        if (beltStack.isEmpty()) return;
                    } else {
                        beltFromAttachment = true;
                    }
                }

                BeltInventoryComponent belt = ModComponents.getBeltInventory(beltStack);
                ItemStack weapon = belt.getItem(beltSlot);
                if (weapon.isEmpty()) return;

                BeltInventoryComponent.Mutable mutable = new BeltInventoryComponent.Mutable(belt);

                if (beltInHand) {
                    // Holding belt in hand: draw weapon, belt goes to inventory/empty slot
                    mutable.setItem(beltSlot, ItemStack.EMPTY);
                    player.setItemInHand(InteractionHand.MAIN_HAND, weapon.copy());
                    if (!player.getInventory().add(handItem)) {
                        player.drop(handItem, false);
                    }
                } else {
                    mutable.setItem(beltSlot, handItem);
                    player.setItemInHand(InteractionHand.MAIN_HAND, weapon.copy());
                }

                ModComponents.setBeltInventory(beltStack, mutable.toImmutable());
                if (beltFromAttachment) {
                    player.setAttached(ModAttachments.BELT_SLOT, beltStack);
                }
            });
        });
    }
}
