package sh.sit.utilitybelt.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import sh.sit.utilitybelt.UtilityBeltMod;

public class UtilityBeltScreen extends AbstractContainerScreen<UtilityBeltScreenHandler> {
    private static final Identifier BELT_LOCATION = Identifier.fromNamespaceAndPath(UtilityBeltMod.MOD_ID, "textures/gui/utility_belt.png");

    public UtilityBeltScreen(UtilityBeltScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BELT_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight, 176, 166);
    }
}
