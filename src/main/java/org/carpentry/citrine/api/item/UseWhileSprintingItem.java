package org.carpentry.citrine.api.item;

import net.minecraft.entity.player.PlayerEntity;

public interface UseWhileSprintingItem {
    boolean canSprint(PlayerEntity user);
}
