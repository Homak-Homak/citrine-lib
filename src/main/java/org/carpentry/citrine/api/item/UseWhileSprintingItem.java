package org.carpentry.citrine.api.item;

import net.minecraft.entity.player.PlayerEntity;

public interface UseWhileSprintingItem {
    // returns if the user can use the item while sprinting
    boolean canSprint(PlayerEntity user);
}
