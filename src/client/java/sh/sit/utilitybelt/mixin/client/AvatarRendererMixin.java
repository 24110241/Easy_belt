package sh.sit.utilitybelt.mixin.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.sit.utilitybelt.attachment.ModAttachments;
import sh.sit.utilitybelt.client.BeltLayer;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(EntityRendererProvider.Context context, boolean slim, CallbackInfo ci) {
        AvatarRenderer<?> renderer = (AvatarRenderer<?>) (Object) this;
        ((LivingEntityRendererAccessor) renderer).invokeAddLayer(new BeltLayer(renderer));
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void onExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float tickDelta, CallbackInfo ci) {
        if (state instanceof AvatarRenderState avatarState && entity instanceof Player player) {
            ItemStack belt = ModAttachments.getBelt(player);
            ((AvatarRenderStateAccessor) avatarState).utilitybelt$setBeltStack(belt);
        }
    }
}
