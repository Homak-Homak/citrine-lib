package org.carpentry.citrine.dev;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import org.carpentry.citrine.api.item.UseWhileSprintingItem;

public class UseWhileSprintItem extends ShieldItem implements UseWhileSprintingItem {
    public UseWhileSprintItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canSprint(PlayerEntity user) {
        return true;
    }
}
