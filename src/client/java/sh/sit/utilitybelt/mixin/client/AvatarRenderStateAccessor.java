package sh.sit.utilitybelt.mixin.client;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AvatarRenderState.class)
public interface AvatarRenderStateAccessor {
    @Accessor("utilitybelt$beltStack")
    ItemStack utilitybelt$getBeltStack();

    @Accessor("utilitybelt$beltStack")
    void utilitybelt$setBeltStack(ItemStack stack);
}
