package sh.sit.utilitybelt.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import sh.sit.utilitybelt.UtilityBeltMod;

public record QuickDrawPayload(int slot) implements CustomPacketPayload {
    public static final Type<QuickDrawPayload> TYPE = new Type<>(
        Identifier.fromNamespaceAndPath(UtilityBeltMod.MOD_ID, "quick_draw")
    );
    public static final StreamCodec<FriendlyByteBuf, QuickDrawPayload> CODEC = StreamCodec.of(
        (buf, payload) -> buf.writeInt(payload.slot),
        buf -> new QuickDrawPayload(buf.readInt())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
