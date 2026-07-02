package sh.sit.utilitybelt.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import sh.sit.utilitybelt.network.QuickDrawPayload;

public class ModKeybindings {
    public static final KeyMapping QUICK_DRAW_1 = new KeyMapping(
        "key.utility-belt.quick_draw_1",
        GLFW.GLFW_KEY_R,
        KeyMapping.Category.GAMEPLAY
    );

    public static final KeyMapping QUICK_DRAW_2 = new KeyMapping(
        "key.utility-belt.quick_draw_2",
        GLFW.GLFW_KEY_G,
        KeyMapping.Category.GAMEPLAY
    );

    public static void register() {
        KeyMappingHelper.registerKeyMapping(QUICK_DRAW_1);
        KeyMappingHelper.registerKeyMapping(QUICK_DRAW_2);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (QUICK_DRAW_1.consumeClick()) {
                ClientPlayNetworking.send(new QuickDrawPayload(0));
            }
            while (QUICK_DRAW_2.consumeClick()) {
                ClientPlayNetworking.send(new QuickDrawPayload(1));
            }
        });
    }
}
