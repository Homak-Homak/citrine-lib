package org.carpentry.citrine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.carpentry.citrine.dev.CitrineItems;
import org.carpentry.citrine.api.item.OnKillItem;
import org.carpentry.citrine.api.item.OnUserDeathItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Citrine implements ModInitializer {
	public static final String MOD_ID = "citrine";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) CitrineItems.init();

		ServerLivingEntityEvents.ALLOW_DEATH.register(((target, damageSource, damageAmount) -> {
			if (damageSource.getAttacker() != null && damageSource.getAttacker() instanceof LivingEntity e) {
				if (e.getMainHandStack().getItem() instanceof OnKillItem i) {
					boolean doKill = i.onKill(e.getMainHandStack(), e, target);
					if (!doKill && target.getHealth() <= 0) target.setHealth(1);
					return doKill;
				}
			}
			if (target.getMainHandStack().getItem() instanceof OnUserDeathItem i) {
				boolean doKill = i.onDeath(target.getMainHandStack(), target);
				if (!doKill && target.getHealth() <= 0) target.setHealth(1);
				return doKill;
			} else if (target.getOffHandStack().getItem() instanceof OnUserDeathItem i) {
				boolean doKill = i.onDeath(target.getMainHandStack(), target);
				if (!doKill && target.getHealth() <= 0) target.setHealth(1);
				return doKill;
			}
			return true;
		}));

		LOGGER.info("Citrine initialized.");
	}

	//TODO: ItemWithSkins, VaryingModelItem, Particle utils, tick schedulers, supporter utils, mod icon, custom font support, unclearable effects, and SO much more 😭😭😭

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
