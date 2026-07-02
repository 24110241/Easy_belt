package sh.sit.utilitybelt.client;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeltModel extends Model<AvatarRenderState> {
    public BeltModel() {
        super(createBeltPart(), RenderTypes::entityCutout);
    }

    private static ModelPart createBeltPart() {
        ModelPart.Cube beltCube = new ModelPart.Cube(
            0, 0,
            -4.0f, 10.0f, -2.0f,
            8.0f, 2.0f, 4.0f,
            0.0f, 0.0f, 0.0f,
            false, 64.0f, 32.0f,
            Set.of()
        );
        ModelPart.Cube pouchCube = new ModelPart.Cube(
            0, 6,
            -3.0f, 12.0f, -2.0f,
            6.0f, 4.0f, 3.0f,
            0.0f, 0.0f, 0.0f,
            false, 64.0f, 32.0f,
            Set.of()
        );
        return new ModelPart(List.of(beltCube, pouchCube), Map.of());
    }

    @Override
    public void setupAnim(AvatarRenderState state) {
    }
}
