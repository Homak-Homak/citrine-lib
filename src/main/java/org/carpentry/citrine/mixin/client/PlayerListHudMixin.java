package org.carpentry.citrine.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.util.SupporterUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @ModifyReturnValue(method = "applyGameModeFormatting", at = @At("RETURN"))
    public Text citrine$applyFormattedName(Text original, PlayerListEntry entry) {
        if (Citrine.CITRINE_SUPPORTERS.isSupporter(entry.getProfile().getId())){
            return SupporterUtils.getStyledName(entry.getProfile().getId(), original);
        }
        return original;
    }
}
