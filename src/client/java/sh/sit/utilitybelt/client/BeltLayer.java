package sh.sit.utilitybelt.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import sh.sit.utilitybelt.UtilityBeltMod;
import sh.sit.utilitybelt.mixin.client.AvatarRenderStateAccessor;

public class BeltLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier BELT_TEXTURE = Identifier.fromNamespaceAndPath(
        UtilityBeltMod.MOD_ID, "textures/entity/belt.png"
    );
    private final BeltModel beltModel = new BeltModel();

    public BeltLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight,
                       AvatarRenderState state, float limbAngle, float limbDistance) {
        if (!(state instanceof AvatarRenderStateAccessor accessor)) return;
        ItemStack beltStack = accessor.utilitybelt$getBeltStack();
        if (beltStack.isEmpty()) return;

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        renderColoredCutoutModel(beltModel, BELT_TEXTURE, poseStack, collector, packedLight, state, -1, -1);
        poseStack.popPose();
    }
}
