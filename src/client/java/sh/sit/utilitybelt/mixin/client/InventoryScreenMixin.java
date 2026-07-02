package sh.sit.utilitybelt.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Inject(method = "extractBackground", at = @At("RETURN"))
    private void renderBeltSlotBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) this;
        int x = accessor.getLeftPos() + 77;
        int y = accessor.getTopPos() + 44;

        graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF8B8B8B);
        graphics.fill(x, y, x + 16, y + 16, 0xFF373737);
    }
}
