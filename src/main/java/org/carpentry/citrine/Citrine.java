package org.carpentry.citrine;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Citrine implements ModInitializer {
	public static final String MOD_ID = "citrine";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Citrine initialized.");
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
