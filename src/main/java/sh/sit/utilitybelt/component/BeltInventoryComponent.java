package sh.sit.utilitybelt.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record BeltInventoryComponent(List<ItemStack> stacks) {
    public static final int BELT_SIZE = 11;

    public static final Codec<BeltInventoryComponent> CODEC = Codec
        .list(ItemStack.OPTIONAL_CODEC)
        .xmap(BeltInventoryComponent::new, BeltInventoryComponent::stacks);

    public static final StreamCodec<RegistryFriendlyByteBuf, BeltInventoryComponent> STREAM_CODEC = StreamCodec
        .composite(
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
            BeltInventoryComponent::stacks,
            BeltInventoryComponent::new
        );

    public static final BeltInventoryComponent DEFAULT = new BeltInventoryComponent(
        NonNullList.withSize(BELT_SIZE, ItemStack.EMPTY)
    );

    public BeltInventoryComponent {
        stacks = new ArrayList<>(stacks);
    }

    public BeltInventoryComponent(NonNullList<ItemStack> stacks) {
        this(new ArrayList<>(stacks));
    }

    public ItemStack getItem(int index) {
        if (index < 0 || index >= stacks.size()) return ItemStack.EMPTY;
        return stacks.get(index);
    }

    public int getContainerSize() {
        return stacks.size();
    }

    public int getRows() {
        return Math.max(1, (int) Math.ceil((float) getContainerSize() / 9));
    }

    public static class Mutable extends SimpleContainer {
        public Mutable(BeltInventoryComponent component) {
            super(component.getContainerSize());
            for (int i = 0; i < component.getContainerSize(); i++) {
                setItem(i, component.getItem(i).copy());
            }
        }

        public BeltInventoryComponent toImmutable() {
            NonNullList<ItemStack> list = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
            for (int i = 0; i < getContainerSize(); i++) {
                list.set(i, getItem(i).copy());
            }
            return new BeltInventoryComponent(list);
        }
    }
}
