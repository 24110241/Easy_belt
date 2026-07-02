package sh.sit.utilitybelt.mixin.client;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin {
    @Unique
    public ItemStack utilitybelt$beltStack = ItemStack.EMPTY;
}
