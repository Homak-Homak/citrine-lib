package org.carpentry.citrine.api.item;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.List;

public interface ItemWithSkins {
    List<Identifier> getSkins();

    default int getCurrentSkin(NbtCompound nbt) {
        return nbt.contains("skin") ? nbt.getInt("skin") : -1;
    }

    default void setSikin(NbtCompound nbt, int skin) {
        nbt.putInt("skin", skin);
    }
}
